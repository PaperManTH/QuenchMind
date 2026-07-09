package com.cuizhi.learning.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.learning.model.po.StudyPlanResource;
import com.cuizhi.learning.service.StudyPlanResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author thpaperman
 * @Description 学习计划-资料关联控制器
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/learning/plan-resource")
@Tag(name = "计划资料关联", description = "计划关联资料管理")
public class StudyPlanResourceController {

    private final StudyPlanResourceService service;

    public StudyPlanResourceController(StudyPlanResourceService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "关联资料到计划")
    public ResponseResult<Void> add(@RequestBody StudyPlanResource pr) {
        service.save(pr);
        return ResponseResult.success();
    }

    @GetMapping("/list")
    @Operation(summary = "计划的关联资料")
    public ResponseResult<List<StudyPlanResource>> listByPlan(@RequestParam String planId) {
        List<StudyPlanResource> list = service.lambdaQuery()
                .eq(StudyPlanResource::getPlanId, planId)
                .orderByAsc(StudyPlanResource::getSortOrder)
                .list();
        return ResponseResult.success(list);
    }

    @DeleteMapping
    @Operation(summary = "取消关联")
    public ResponseResult<Void> remove(@RequestParam String planId, @RequestParam String resourceId) {
        service.lambdaUpdate()
                .eq(StudyPlanResource::getPlanId, planId)
                .eq(StudyPlanResource::getResourceId, resourceId)
                .remove();
        return ResponseResult.success();
    }
}
