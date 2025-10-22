package org.idea.qiyu.live.framework.datasource.starter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 做成配置，实现通过参数来触发Hikari数据源的初始化
 *
 * @Author idea
 * @Date: Created in 18:06 2023/5/7
 * @Description
 */
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);
    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info(" ==================  [ShardingJdbcDatasourceAutoInitConnectionConfig] dataSource: {}", dataSource);
            //手动触发下连接池的连接创建
            Connection connection = dataSource.getConnection();
        };
    }
}