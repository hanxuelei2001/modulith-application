package com.example.modulith.common.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

@Configuration
public class DataBaseConfig {

    /**
     * 获取数据源
     * 配置 zipkin, 可以对服务进行链路追踪
     *
     * @param dataSourceProperties 数据源属性
     * @return {@link MysqlDataSource}
     * @throws SQLException SQLException
     */
    @Bean
    public HikariDataSource getDataSource(DataSourceProperties dataSourceProperties) throws SQLException {
        // 如果 当前的 数据库是 mysql,那么为 mysql 添加 queryInterceptor
        HikariDataSource dataSource = new HikariDataSource();
        // 设置 driver
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // 设置 url
        dataSource.setJdbcUrl(dataSourceProperties.getUrl());
        // 设置 username
        dataSource.setUsername(dataSourceProperties.getUsername());
        // 设置 password
        dataSource.setPassword(dataSourceProperties.getPassword());
        // 设置线程池
        Thread.Builder.OfVirtual builder = Thread.ofVirtual();
        // 定义 build 的名称
        builder.name("virtual-datasource");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
                        builder.factory());
        dataSource.setScheduledExecutor(scheduledThreadPoolExecutor);
        // 设置 thread factory
        ThreadFactory factory = builder.factory();
        dataSource.setThreadFactory(factory);
        return dataSource;
    }
}
