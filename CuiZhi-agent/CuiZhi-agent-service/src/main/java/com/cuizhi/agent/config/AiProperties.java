package com.cuizhi.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: thpaperman
 * @Date: 2026/5/10 20:31
 * @Description: 读取系统提示词配置
 * @Version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cz.ai.prompt")
public class AiProperties {

    private System system;

    @Data
    public static class System {
        private Chat chat;

        @Data
        public static class Chat {
            private String dataId;
            // 读取 Nacaos 数据源的分组名称, 默认 DEFAULT_GROUP
            private String group = "DEFAULT_GROUP";
            // 读取 Nacaos 数据源超时时间，单位毫秒，默认 20 秒
            private long timeoutMs = 20000L;
        }
    }
}
