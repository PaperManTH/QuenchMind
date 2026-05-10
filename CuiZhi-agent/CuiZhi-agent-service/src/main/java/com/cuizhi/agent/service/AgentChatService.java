package com.cuizhi.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuizhi.agent.model.po.AiMessage;
import com.cuizhi.agent.model.vo.ChatVO;
import reactor.core.publisher.Flux;

/**
 * @Author: thpaperman
 * @Date: 2026/5/5 21:55
 * @Description: AI 对话接口
 * @Version: 1.0
 */
public interface AgentChatService extends IService<AiMessage> {

    /**
     * 对话生成
     *
     * @param sessionId 会话ID
     * @param message 消息
     * @param modelName 模型名称
     * @return  聊天结果
     */
    Flux<ChatVO> chat(String sessionId, String message, String modelName);


}
