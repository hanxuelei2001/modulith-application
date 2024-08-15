package com.example.modulith.config;

import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@EnableAsync
@Configuration
public class TomcatCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Bean
    public SimpleAsyncTaskExecutor applicationTaskExecutor() {
        // 定义 SimpleAsyncTaskExecutor
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        // 设置线程工厂
        Thread.Builder.OfVirtual builder = Thread.ofVirtual();
        builder.name("simpleAsyncTask");
        executor.setThreadFactory(builder.factory());
        // 设置线程名称前缀
        executor.setThreadNamePrefix("applicationTaskExecutor-");
        return executor;
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        if (factory instanceof TomcatServletWebServerFactory tomcatFactory) {

            // 创建自定义线程池
            Executor executor = virtualThreadExecutor();

            // 设置自定义线程池
            tomcatFactory.addConnectorCustomizers(connector -> {
                // 获取协议处理器
                ProtocolHandler protocolHandler = connector.getProtocolHandler();
                // 从协议处理器中获取线程池
                protocolHandler.setExecutor(executor);
            });
        }
    }

    @Bean
    public Executor virtualThreadExecutor() {
        // 创建自定义线程池，并返回
        // 这里可以根据需要进行线程池的配置
        Thread.Builder.OfVirtual builder = Thread.ofVirtual();
        // 定义线程名称
        builder.name("tomcat-virtual-thread");
        // 获取线程工厂
        ThreadFactory factory = builder.factory();
        // 创建线程池
        return Executors.newThreadPerTaskExecutor(factory);
    }
}