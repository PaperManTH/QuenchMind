package com.cuizhi.learning.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 错题复习表
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_wrong_question")
public class WrongQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 来源资料ID
     */
    private Long resourceId;

    /**
     * 关联知识点ID
     */
    private Long knowledgeId;

    /**
     * 题目
     */
    private String question;

    /**
     * 题型
     */
    private String questionType;

    /**
     * 难度 1-5
     */
    private Integer difficulty;

    /**
     * 标准答案
     */
    private String correctAnswer;

    /**
     * 用户回答
     */
    private String userAnswer;

    /**
     * 是否答对
     */
    private Integer isCorrect;

    /**
     * 复习次数
     */
    private Integer reviewCount;

    /**
     * 下次复习时间
     */
    private LocalDateTime nextReviewAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
