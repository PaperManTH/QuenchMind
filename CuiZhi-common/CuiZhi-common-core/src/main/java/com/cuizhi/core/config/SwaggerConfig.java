package com.cuizhi.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: thpaperman
 * @Date: 2026/5/11
 * @Description: SpringDoc OpenAPI 配置
 * @Version: 1.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("淬知 QuenchMind 智能学习助手")
                        .description("淬知系统接口文档")
                        .version("1.0.0"));
    }
}
