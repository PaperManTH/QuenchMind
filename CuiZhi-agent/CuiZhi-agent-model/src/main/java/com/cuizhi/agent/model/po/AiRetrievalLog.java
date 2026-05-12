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
 * @Description: RAG 检索日志表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_ai_retrieval_log")
public class AiRetrievalLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID（可空）
     */
    private Long sessionId;

    /**
     * 关联消息ID（可空）
     */
    private Long messageId;

    /**
     * 检索 query 文本
     */
    private String query;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}

