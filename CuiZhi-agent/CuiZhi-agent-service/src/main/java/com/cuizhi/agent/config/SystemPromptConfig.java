package com.cuizhi.agent.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.ai.AiService;
import com.alibaba.nacos.api.ai.listener.AbstractNacosPromptListener;
import com.alibaba.nacos.api.ai.listener.NacosPromptEvent;
import com.alibaba.nacos.api.ai.model.prompt.Prompt;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
    private AiService aiService;

    @Value("${cz.agent.prompt.chat.key:CZ_Agent_System_Prompt}")
    private String promptKey;

    // 原子引用, 保证线程安全
    private final AtomicReference<Prompt> chatSystemPrompt = new AtomicReference<>();

    @PostConstruct
    public void init() {
        loadSystemPrompt(chatSystemPrompt);
    }

    private void loadSystemPrompt(AtomicReference<Prompt> chatSystemPrompt) {
        try {
            // 获取 Nacos 中的系统提示词配置
            Prompt systemPrompt = aiService.getPrompt(promptKey);
            chatSystemPrompt.set(systemPrompt);
            log.info("[Nacos] 获取系统提示词成功, [内容] : {}", systemPrompt);

            // 监听事件, 进行热更新
            aiService.subscribePrompt(promptKey, null, null, new AbstractNacosPromptListener() {
                @Override
                public void onEvent(NacosPromptEvent event) {
                    try {
                        Prompt newPrompt = aiService.getPrompt(event.getPromptKey());
                        chatSystemPrompt.set(newPrompt);
                        log.info("[Nacos Prompt] 系统提示词已热更新, key={}, version={}",
                                newPrompt.getPromptKey(), newPrompt.getVersion());
                    } catch (NacosException e) {
                        log.error("[Nacos Prompt] 热更新失败", e);
                    }
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
