package com.cuizhi.learning.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 学习任务表
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_study_task")
public class StudyTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 学习日期
     */
    private LocalDate taskDate;

    /**
     * 任务顺序
     */
    private Integer taskOrder;

    /**
     * 任务类型：阅读/练习/复习/测验
     */
    private String taskType;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 优先级 1高 2中 3低
     */
    private Integer priority;

    private String status;

    private LocalDateTime completedAt;

    /**
     * 完成评分
     */
    private BigDecimal score;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
