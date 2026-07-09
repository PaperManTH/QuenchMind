package com.cuizhi.learning.service;

import com.cuizhi.learning.model.dto.GenerateRequest;
import com.cuizhi.learning.model.dto.GenerateResult;

/**
 * @Author thpaperman
 * @Description AI 学习计划生成服务
 * @Date 2026/7/7
 * @Version 1.0
 */
public interface StudyPlanGenerateService {

    /**
     * 根据学习目标调用 Python AI 生成学习计划
     */
    GenerateResult generate(GenerateRequest request);
}
