package com.cuizhi.agent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: thpaperman
 * @Date: 2026/5/6 22:15
 * @Description: 聊天结果返回信息
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "聊天结果返回信息")
public class ChatVO {

    @Schema(description = "聊天结果")
    private String data;

    @Schema(description = "事件类型")
    private Integer eventType;
}
