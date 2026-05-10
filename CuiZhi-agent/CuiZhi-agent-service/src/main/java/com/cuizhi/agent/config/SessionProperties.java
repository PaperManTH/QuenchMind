package com.cuizhi.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: thpaperman
 * @Date: 2026/5/3 20:59
 * @Description: AI会话元数据配置
 * @Version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cz.ai.session")
public class SessionProperties {

    /**
     * AI 助手的名称
     */
    private String aiName;

    /**
     * AI 助手的描述
     */
    private String description;

    /**
     * AI 助手的图标
     */
    private String icon;
}
