package com.cuizhi.agent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuizhi.agent.mapper.AgentMessageMapper;
import com.cuizhi.agent.model.enums.ChatEventType;
import com.cuizhi.agent.model.po.AiMessage;
import com.cuizhi.agent.model.vo.ChatVO;
import com.cuizhi.agent.service.AgentChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @Author: thpaperman
 * @Date: 2026/5/6 21:08
 * @Description: AI 对话实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class AgentChatServiceImpl extends ServiceImpl<AgentMessageMapper, AiMessage> implements AgentChatService {

    @Autowired
    private ChatClient chatClient;

    @Override
    public Flux<ChatVO> chat(String sessionId, String message, String modelName) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .chatResponse()
                .map(res -> {
                    String text = res.getResult().getOutput().getText();
                    return ChatVO.builder().data(text).eventType(ChatEventType.CHAT_MESSAGE.getEventCode()).build();
                })
                .concatWith(Flux.just(ChatVO.builder().data("[DONE]").eventType(ChatEventType.CHAT_END.getEventCode()).build()));
    }
}
