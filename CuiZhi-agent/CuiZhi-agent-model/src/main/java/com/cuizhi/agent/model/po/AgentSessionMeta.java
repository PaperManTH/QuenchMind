package com.cuizhi.agent.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/3 20:59
 * @Description: AI会话元数据表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_agent_session_meta")
public class AgentSessionMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话唯一标识（UUID格式）
     */
    private String sessionId;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 会话名称
     */
    private String sessionName;

    /**
     * 会话类型: LEARNING_QA学习问答/TASK_GENERATION任务生成/WRONG_QUESTION错题分析
     */
    private String conversationType;

    /**
     * 状态: 1正常 0关闭 2归档
     */
    private Integer status;

    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageAt;

    /**
     * 消息总数
     */
    private Integer messageCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 软删除时间
     */
    private LocalDateTime deletedAt;
}
