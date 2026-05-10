package com.cuizhi.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author thpaperman
 * @Description SpringAI alibaba 配置类
 * @Date 2026/4/16
 * @Version 1.0
 */
@Configuration
public class SpringAiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 Advisor loggerAdvisor) {
        return chatClientBuilder
                .defaultAdvisors(loggerAdvisor)
                .build();
    }

    @Bean
    public Advisor loggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }
}
