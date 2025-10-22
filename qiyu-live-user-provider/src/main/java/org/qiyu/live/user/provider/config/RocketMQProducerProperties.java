package org.qiyu.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @param
 * @Auther:北
 * @Date:2025/10/17
 * @Description: 生产者的配置信息
 * @VERSON:1.0
 */
// 配置属性在application.yml配置时，都会加上这个前缀
@ConfigurationProperties(prefix = "qiyu.rmq.producer")
@Configuration
public class RocketMQProducerProperties {

    // rocketmq的nameSever地址
    private String nameSrv;
    // 分组名称
    private String groupName;
    // 消息重发次数
    private int retryTimes;
    private int sendTimeOut;

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

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getSendTimeOut() {
        return sendTimeOut;
    }

    public void setSendTimeOut(int sendTimeOut) {
        this.sendTimeOut = sendTimeOut;
    }

    @Override
    public String toString() {
        return "RocketMQProducerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                ", retryTimes=" + retryTimes +
                ", sendTimeOut=" + sendTimeOut +
                '}';
    }
}
