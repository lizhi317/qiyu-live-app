package org.qiyu.live.user.provider.config;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.*;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/17
 * @Description:RocketMQ的生产者bean配置类
 * @VERSON:1.0
 */

@Configuration
public class RocketMQProducerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQProducerConfig.class);

    @Resource
    private RocketMQProducerProperties producerProperties;
    @Value("${spring.application.name}")
    private String applicationName;

    // Bean注入
    @Bean
    public MQProducer mqProducer() {
        // 自定义一个线程池
        ThreadPoolExecutor asyncThreadPoolExecutor = new ThreadPoolExecutor(100, 150, 3, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                // 线程工厂实际的线程生成
                Thread thread = new Thread(r);
                // 设置名字，方便查看哪个业务线程有问题，有很大的好处
                thread.setName(applicationName + ":rmp-producer-thread" + ThreadLocalRandom.current().nextInt());
                return thread;
            }
        });
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        try {
            defaultMQProducer.setNamesrvAddr(producerProperties.getNameSrv());
            defaultMQProducer.setProducerGroup(producerProperties.getGroupName());
            defaultMQProducer.setRetryTimesWhenSendFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setRetryTimesWhenSendAsyncFailed(producerProperties.getRetryTimes());
            defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
            // 设置异步发送的自定义线程池
            defaultMQProducer.setAsyncSenderExecutor(asyncThreadPoolExecutor);
            defaultMQProducer.start();
            LOGGER.info("MQ生产者启动成功, nameSrv is {}", producerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        return defaultMQProducer;
    }
}
