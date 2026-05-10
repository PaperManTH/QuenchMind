package com.cuizhi.agent.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: thpaperman
 * @Date: 2026/5/10 20:20
 * @Description: 系统提示词读取配置
 * @Version: 1.0
 */
@Slf4j
@Getter
@Configuration
public class SystemPromptConfig {

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Autowired
    private AiProperties aiProperties;

    // 原子引用, 保证线程安全
    private final AtomicReference<String> chatSystemPrompt = new AtomicReference<>();

    @PostConstruct
    public void init() {
        loadSystemPrompt(aiProperties.getSystem().getChat(), chatSystemPrompt);
    }

    private void loadSystemPrompt(AiProperties.System.Chat chatConfig, AtomicReference<String> chatSystemPrompt) {
        try {

            String dataId = chatConfig.getDataId();
            String group = chatConfig.getGroup();
            long timeoutMs = chatConfig.getTimeoutMs();
            // 获取 Nacos 中的系统提示词配置
            String systemPrompt = nacosConfigManager.getConfigService().getConfig(dataId, group, timeoutMs);
            chatSystemPrompt.set(systemPrompt);

            log.info("[Nacos] 获取系统提示词成功, [内容] : {}", systemPrompt);

            // 监听事件, 进行热更新
            nacosConfigManager.getConfigService().addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {return null;}

                @Override
                public void receiveConfigInfo(String configInfo) {
                    chatSystemPrompt.set(configInfo);
                    log.info("[Nacos] 系统提示词热更新成功, [内容] : {}", systemPrompt);
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
