package org.qiyu.live.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @param
 * @Auther:北
 * @Date:2025/8/25
 * @Description:
 * @VERSON:1.0
 */

@SpringBootApplication
@EnableDiscoveryClient
public class ApiWebApplication {
    public static void main(String[] args) {
        // 用new的写法更推荐
        SpringApplication springApplication = new SpringApplication(ApiWebApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        springApplication.run(args);
    }
}
