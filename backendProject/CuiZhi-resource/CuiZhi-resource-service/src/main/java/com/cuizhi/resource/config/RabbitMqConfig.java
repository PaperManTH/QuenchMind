package com.cuizhi.resource.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author thpaperman
 * @Description RabbitMQ 队列 — Java(Upload) → Python(解析+分块+向量化+摘要)
 * @Date 2026/7/7
 * @Version 1.0
 */
@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_DOC_PROCESS = "document.process";

    @Bean
    public Queue documentProcessQueue() {
        return new Queue(QUEUE_DOC_PROCESS, true);
    }
}
