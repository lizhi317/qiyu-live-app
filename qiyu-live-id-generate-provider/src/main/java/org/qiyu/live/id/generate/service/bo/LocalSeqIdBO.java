package org.qiyu.live.id.generate.service.bo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/21
 * @Description:
 * @VERSON:1.0
 */
public class LocalSeqIdBO {

    private int id;
    /**
     * 在内存中记录的当前有序ID的值, 使用原子类
     */
    private AtomicLong currentNum;

    /**
     * 当前id段的开始值
     */
    private Long currentStart;

    /**
     * 当前id段的结束值
     */
    private Long nextThreshold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AtomicLong getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(AtomicLong currentNum) {
        this.currentNum = currentNum;
    }

    public Long getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(Long currentStart) {
        this.currentStart = currentStart;
    }

    public Long getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(Long nextThreshold) {
        this.nextThreshold = nextThreshold;
    }
}
