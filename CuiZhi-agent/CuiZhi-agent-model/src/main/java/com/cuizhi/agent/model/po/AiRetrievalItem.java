package com.cuizhi.agent.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: thpaperman
 * @Date: 2026/5/3 20:59
 * @Description: 向量检索结果明细表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_ai_retrieval_item")
public class AiRetrievalItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 检索ID
     */
    private Long retrievalId;

    /**
     * 切块ID
     */
    private Long chunkId;

    /**
     * 相似度/得分（值域由算法决定）
     */
    private Double score;
}

