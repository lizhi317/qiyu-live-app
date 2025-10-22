package org.qiyu.live.id.generate.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.qiyu.live.id.generate.interfaces.IdGenerateRpc;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/21
 * @Description:
 * @VERSON:1.0
 */
@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {
    @Override
    public Long getSeqId(Integer id) {
        return 0L;
    }

    @Override
    public Long getSeqId(Long id) {
        return 0L;
    }
}
