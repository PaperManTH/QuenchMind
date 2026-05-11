package com.cuizhi.agent.config;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.ai.AiFactory;
import com.alibaba.nacos.api.ai.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: thpaperman
 * @Date: 2026/5/11
 * @Description: Nacos AI 服务配置
 * @Version: 1.0
 */
@Configuration
public class NacosAiConfig {

    @Value("${spring.cloud.nacos.config.server-addr:localhost:8848}")
    private String serverAddr;
    
    @Value("${spring.cloud.nacos.config.namespace:CZ_QuenchMind}")
    private String namespace;
    
    @Bean
    public AiService aiService() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
        
        return AiFactory.createAiService(properties);
    }
}
