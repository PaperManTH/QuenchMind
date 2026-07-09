package com.cuizhi.learning.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.learning.model.po.StudyPlan;
import com.cuizhi.learning.service.StudyPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author thpaperman
 * @Description 学习计划控制器
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/learning/study-plan")
@Tag(name = "学习计划", description = "学习计划 CRUD")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询计划详情")
    public ResponseResult<StudyPlan> getById(@PathVariable String id) {
        return ResponseResult.success(studyPlanService.getById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "用户的计划列表")
    public ResponseResult<List<StudyPlan>> listByUser(@RequestParam String userId) {
        List<StudyPlan> list = studyPlanService.lambdaQuery()
                .eq(StudyPlan::getUserId, userId)
                .orderByDesc(StudyPlan::getCreatedAt)
                .list();
        return ResponseResult.success(list);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新计划")
    public ResponseResult<Void> update(@PathVariable String id, @RequestBody StudyPlan plan) {
        plan.setId(id);
        studyPlanService.updateById(plan);
        return ResponseResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除计划")
    public ResponseResult<Void> delete(@PathVariable String id) {
        studyPlanService.removeById(id);
        return ResponseResult.success();
    }
}
