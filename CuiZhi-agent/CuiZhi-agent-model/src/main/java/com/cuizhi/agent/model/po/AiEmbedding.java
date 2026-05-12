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
 * @Date: 2026/5/3 20:59
 * @Description: AI向量表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_ai_embedding")
public class AiEmbedding implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 切片 id
     */
    private Long chunkId;

    /**
     * 矢量值。目前保持字符串形式，以避免对 pgvector Java 类型的硬依赖。典型格式：“[0.1,0.2,...]”
     */
    private String embedding;

    /**
     * 嵌入模型名称
     */
    private String modelName;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}

