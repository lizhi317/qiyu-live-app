package org.qiyu.live.id.generate.interfaces;

public interface IdGenerateRpc {
    /**
     * 获取有序ID
     * @param id
     * @return
     */
    Long getSeqId(Integer id);


    /**
     * 获取无序Id
     * @param id
     * @return
     */
    Long getSeqId(Long id);
}