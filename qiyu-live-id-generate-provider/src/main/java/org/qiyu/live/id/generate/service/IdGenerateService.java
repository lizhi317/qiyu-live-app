package org.qiyu.live.id.generate.service;

import org.springframework.stereotype.Service;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/21
 * @Description:
 * @VERSON:1.0
 */
@Service
public interface IdGenerateService {
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
