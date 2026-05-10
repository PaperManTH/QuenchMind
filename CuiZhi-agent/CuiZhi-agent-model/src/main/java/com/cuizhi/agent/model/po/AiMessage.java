package com.cuizhi.agent.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/5 22:04
 * @Description: AI会话消息表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_ai_message")
public class AiMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话唯一标识（UUID格式）
     */
    private String sessionId;

    /**
     * 消息角色
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 消耗 Token
     */
    private Integer tokenCount;

    /**
     * 延迟 ms
     */
    private Integer latency_ms;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
