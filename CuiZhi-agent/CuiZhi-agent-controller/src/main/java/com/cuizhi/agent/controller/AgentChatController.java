package com.cuizhi.agent.controller;

import com.cuizhi.agent.model.dto.ChatDTO;
import com.cuizhi.agent.model.vo.ChatVO;
import com.cuizhi.agent.service.AgentChatService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author thpaperman
 * @Description 对话生成控制器
 * @Date 2026/4/21
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/agchat")
public class AgentChatController {

    @Autowired
    private AgentChatService chatService;

    /**
     * 普通对话
     * @param chatDTO 聊天信息
     * @return 聊天结果
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatVO> chat(@RequestBody @Valid ChatDTO chatDTO) {
        return chatService.chat(
                chatDTO.getSessionId(),
                chatDTO.getMessage(),
                chatDTO.getModelName());
    }


}
