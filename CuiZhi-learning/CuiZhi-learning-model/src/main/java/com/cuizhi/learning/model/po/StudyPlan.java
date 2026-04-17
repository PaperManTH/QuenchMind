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
 * 学习计划表
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_study_plan")
public class StudyPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 来源资料ID
     */
    private Long sourceResourceId;

    /**
     * 计划标题
     */
    private String title;

    /**
     * 学习目标
     */
    private String goal;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 总天数
     */
    private Integer totalDays;

    /**
     * 每日学习时长（分钟）
     */
    private Integer dailyMinutes;

    /**
     * 进度百分比
     */
    private BigDecimal progress;

    private String status;

    /**
     * 生成所用模型
     */
    private String aiModel;

    /**
     * Prompt版本
     */
    private String promptVersion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
