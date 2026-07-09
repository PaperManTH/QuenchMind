package com.cuizhi.learning.model.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @Author thpaperman
 * @Description AI 生成学习计划结果
 * @Date 2026/7/7
 * @Version 1.0
 */
@Data
public class GenerateResult {
    private String planId;
    private String title;
    private List<Map<String, Object>> tasks;
}
