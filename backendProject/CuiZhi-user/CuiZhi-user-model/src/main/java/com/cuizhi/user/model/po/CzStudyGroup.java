package com.cuizhi.user.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 学习小组表
 * @Version: 1.0
 */
@Data
@TableName("cz_study_group")
public class CzStudyGroup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 小组ID（UUID字符串格式）
     */
    private String id;

    /**
     * 小组名称
     */
    private String groupName;

    /**
     * 小组描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private String ownerId;

    /**
     * 最大成员数
     */
    private Integer maxMembers;

    /**
     * 状态 1正常 0禁用
     */
    private Short status;

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
     * 删除时间（逻辑删除）
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime deletedAt;
}
