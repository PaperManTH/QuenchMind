package com.cuizhi.agent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/3 20:59
 * @Description: 创建会话返回信息
 * @Version: 1.0
 */
@Data
@Schema(description = "创建会话返回信息")
public class SessionVO {

    @Schema(description = "AI 助手的名称")
    private String aiName;
    
    @Schema(description = "AI 助手的描述")
    private String description;
    
    @Schema(description = "AI 助手的图标")
    private String icon;
    
    @Schema(description = "会话唯一标识(UUID格式)")
    private String sessionId;
    
    @Schema(description = "关联用户ID")
    private Long userId;
    
    @Schema(description = "会话名称")
    private String sessionName;
    
    @Schema(description = "会话类型: LEARNING_QA学习问答/TASK_GENERATION任务生成/WRONG_QUESTION错题分析")
    private String conversationType;
    
    @Schema(description = "状态: 1正常 0关闭 2归档")
    private Integer status;
    
    @Schema(description = "最后一条消息时间")
    private LocalDateTime lastMessageAt;
    
    @Schema(description = "消息总数")
    private Integer messageCount;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
