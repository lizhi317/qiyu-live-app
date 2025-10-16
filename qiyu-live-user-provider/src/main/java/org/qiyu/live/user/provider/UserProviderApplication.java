package org.qiyu.live.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @param
 * @Auther:北
 * @Date:2025/8/23
 * @Description:用户中台的Dubbo服务提供者
 *
 * @VERSON:1.0
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication {

    public static void main(String[] args) {
        // 一种比较好的启动方式
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        // 不调用网络插件，纯粹的spring容器
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
