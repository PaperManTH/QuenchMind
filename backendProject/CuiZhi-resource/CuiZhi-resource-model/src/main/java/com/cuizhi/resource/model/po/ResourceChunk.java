package com.cuizhi.resource.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 原始文本切块表（用于向量检索）
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_resource_chunk")
public class ResourceChunk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 所属资料ID
     */
    private String resourceId;

    /**
     * 分块序号
     */
    private Integer chunkNo;

    /**
     * 起始页
     */
    private Integer pageStart;

    /**
     * 结束页
     */
    private Integer pageEnd;

    /**
     * 原始文本内容
     */
    private String content;

    /**
     * Token数量
     */
    private Integer tokenCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
