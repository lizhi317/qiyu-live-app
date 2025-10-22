package org.qiyu.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/17
 * @Description:
 * @VERSON:1.0
 */

@ConfigurationProperties(prefix = "qiyu.rmq.consumer")
@Configuration
public class RocketMQConsumerProperties {

    // rocketmq的nameSever地址
    private String nameSrv;
    // 分组名称
    private String groupName;

    public String getNameSrv() {
        return nameSrv;
    }

    public void setNameSrv(String nameSrv) {
        this.nameSrv = nameSrv;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "RocketMQConsumerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
