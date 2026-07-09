package com.cuizhi.learning.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.learning.model.dto.GenerateRequest;
import com.cuizhi.learning.model.dto.GenerateResult;
import com.cuizhi.learning.service.StudyPlanGenerateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author thpaperman
 * @Description AI 学习计划生成控制器
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/learning/plan")
@Tag(name = "AI 学习计划", description = "AI 生成学习计划")
public class StudyPlanGenerateController {

    private final StudyPlanGenerateService generateService;

    public StudyPlanGenerateController(StudyPlanGenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping("/generate")
    @Operation(summary = "AI 生成学习计划", description = "根据学习目标调用 Python AI 生成学习计划")
    public ResponseResult<GenerateResult> generate(@RequestBody GenerateRequest request) {
        return ResponseResult.success(generateService.generate(request));
    }
}
