package org.qiyu.live.id.generate.service.Impl;

import jakarta.annotation.Resource;
import org.qiyu.live.id.generate.dao.mapper.IdGenerateMapper;
import org.qiyu.live.id.generate.dao.po.IdGeneratePO;
import org.qiyu.live.id.generate.service.IdGenerateService;
import org.qiyu.live.id.generate.service.bo.LocalSeqIdBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/21
 * @Description:
 * @VERSON:1.0
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    @Resource
    private IdGenerateMapper idGenerateMapper;

    private static Map<Integer, LocalSeqIdBO> localSeqIdBOMap = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateServiceImpl.class);

    // 使用异步的线程池调用
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                    return thread;
                }
            });

    // 阈值常量
    private static final float UPDATE_RATE = 0.75F;

    // 限流, 一次只有一个线程进入这个任务
    private static final Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error, id is {}]", id);
            return null;
        }
        LocalSeqIdBO localSeqIdBO = localSeqIdBOMap.get(id);
        if (localSeqIdBO == null) {
            LOGGER.error("[getSeqId] localSeqIdBO is null, id is {}]", id);
            return null;
        }
        this.refreshLocalSeqId(localSeqIdBO);
        if (localSeqIdBO.getCurrentNum().get() > localSeqIdBO.getNextThreshold()){
            // 同步刷新不好，容易占用dubbo线程池，导致其他
            LOGGER.error("[getSeqId] id is over limit, id is {}", id);
            return null;
        }
        // 不能光自增，还要同步更新mysql占用
        long returnId = localSeqIdBO.getCurrentNum().getAndIncrement();
        return returnId;
    }

    /**
     * 按照阈值刷新本地有序id段
     *
     * @param localSeqIdBO
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        // 超过使用id段的阈值
        if (localSeqIdBO.getCurrentNum().get() - localSeqIdBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null, is is {}", localSeqIdBO.getId());
                return;
            }
            // 使用拦截操作
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("开始尝试进行本地id段的同步操作");
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    // 使用异步线程去同步id段操作
                    public void run() {
                        IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localSeqIdBO.getId());
                        tryUpdateMySQLRecord(idGeneratePO);
                        semaphoreMap.get(localSeqIdBO.getId()).release();
                        LOGGER.info("本地id段同步完成");
                    }
                });
            }
        }
    }

    @Override
    public Long getSeqId(Long id) {
        return 0L;
    }

    // Bean初始化的时候就会调用
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGeneratePO> idGeneratePOList = idGenerateMapper.selectAll();
        for (IdGeneratePO idGeneratePO : idGeneratePOList) {
            LOGGER.info("服务更启动，抢占新的id段");
            tryUpdateMySQLRecord(idGeneratePO);
            // 每次做异步的时候只能有一个线程可以做
            semaphoreMap.put(idGeneratePO.getId(), new Semaphore(1));
        }
    }

    /**
     * 启动的时候就需要更新MySQL里面的分布式id的配置信息，更新边界。占用相应的id段。
     * 成功则注入使用区间；失败则重试
     * 同步执行，会有很多的网络ID，性能较慢
     *
     * @param idGeneratePO
     */
    private void tryUpdateMySQLRecord(IdGeneratePO idGeneratePO) {
        long currentStart = idGeneratePO.getCurrentStart();
        long nextThreshold = idGeneratePO.getNextThreshold();
        long currentNum = currentStart;
        int updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        if (updateResult > 0) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            AtomicLong atomicLong = new AtomicLong(currentNum);
            localSeqIdBO.setId(idGeneratePO.getId());
            localSeqIdBO.setCurrentNum(atomicLong);
            // 写入新的东西
            localSeqIdBO.setCurrentStart(currentStart);
            localSeqIdBO.setNextThreshold(nextThreshold);
            // 初始化的时候就要进行填充
            localSeqIdBOMap.put(localSeqIdBO.getId(), localSeqIdBO);
            return;
        }
        // 更新失败，三次重试，见于version被修改之后
        for (int i = 0; i < 3; i++) {
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult > 0) {
                LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
                AtomicLong atomicLong = new AtomicLong(idGeneratePO.getCurrentStart());
                localSeqIdBO.setId(idGeneratePO.getId());
                localSeqIdBO.setCurrentNum(atomicLong);
                // 写入新的东西
                localSeqIdBO.setCurrentStart(idGeneratePO.getCurrentStart());
                localSeqIdBO.setNextThreshold(idGeneratePO.getNextThreshold());
                // 初始化的时候就要进行填充
                localSeqIdBOMap.put(localSeqIdBO.getId(), localSeqIdBO);
                break;
            }
        }
        throw new RuntimeException("表id段占用失败，id is " + idGeneratePO.getId());

    }
}
