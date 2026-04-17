package com.cuizhi.learning.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 学习计划-资料关联表（多对多）
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_study_plan_resource")
public class StudyPlanResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long planId;

    private Long resourceId;

    private Integer sortOrder;

    private LocalDateTime createdAt;


}
