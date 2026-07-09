package com.cuizhi.core.config;

import com.cuizhi.core.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author thpaperman
 * @Description 注册 JwtAuthFilter — 配置 jwt.secret 即自动生效，不依赖 Spring Security
 * @Date 2026/7/7
 * @Version 1.0
 */
@Configuration
@ConditionalOnProperty("jwt.secret")
public class JwtAuthConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration() {
        FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtAuthFilter(secret));
        reg.addUrlPatterns("/*");
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        reg.setName("jwtAuthFilter");
        return reg;
    }
}
