package com.cuizhi.learning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author thpaperman
 * @Description Python AI 引擎连接配置
 * @Date 2026/7/7
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai.engine")
public class AiEngineProperties {
    private String baseUrl = "http://localhost:18023/ai";
    private int connectTimeout = 5000;
    private int readTimeout = 120000;
}
