package com.cuizhi.agent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: thpaperman
 * @Date: 2026/5/6 21:20
 * @Description: 对话参数信息
 * @Version: 1.0
 */
@Data
@Schema(description = "对话参数信息")
public class ChatDTO {

    @Schema(description = "会话ID", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "会话ID不能为空")
    private String sessionId;

    @Schema(description = "消息内容", example = "请帮我解释什么是机器学习", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "消息内容不能为空")
    private String message;

    @Schema(description = "使用模型名称", example = "gpt-4", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模型名称不能为空")
    private String modelName;
}