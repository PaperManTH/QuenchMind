package com.cuizhi.learning.model.dto;

import lombok.Data;

/**
 * @Author thpaperman
 * @Description AI 生成学习计划请求
 * @Date 2026/7/7
 * @Version 1.0
 */
@Data
public class GenerateRequest {
    private String userId;
    private String goal;
    private int days;
    private int dailyMinutes;
}
