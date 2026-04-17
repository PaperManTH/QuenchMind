package com.cuizhi.resource.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * AI提炼的知识点表（结构化知识）
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_resource_knowledge")
public class ResourceKnowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属资料ID
     */
    private Long resourceId;

    /**
     * 关联的原始切块ID
     */
    private Long chunkId;

    /**
     * 知识点标题
     */
    private String title;

    /**
     * 知识点摘要
     */
    private String summary;

    /**
     * 关键词，逗号分隔
     */
    private String keywords;

    /**
     * 难度等级 1-5
     */
    private Integer difficulty;

    /**
     * 知识分类
     */
    private String category;

    /**
     * 重要程度 0.00-1.00
     */
    private BigDecimal importance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
