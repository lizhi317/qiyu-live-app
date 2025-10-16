package org.idea.qiyu.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/8
 * @Description:
 * @VERSON:1.0
 */
//定义成配置类
@Configuration

//条件注入，在Bean注入的时候，先考虑返回值
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static String USER_INFO_KEY = "userInfo";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }


}
