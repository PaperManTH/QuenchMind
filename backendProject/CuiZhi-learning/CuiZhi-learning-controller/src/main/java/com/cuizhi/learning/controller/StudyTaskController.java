package com.cuizhi.learning.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.learning.model.po.StudyTask;
import com.cuizhi.learning.service.StudyTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学习任务控制器
 */
@Slf4j
@RestController
@RequestMapping("/learning/study-task")
@Tag(name = "学习任务", description = "学习任务 CRUD + 进度")
@AllArgsConstructor
public class StudyTaskController {

    private final StudyTaskService studyTaskService;

    @GetMapping("/list")
    @Operation(summary = "计划的全部任务")
    public ResponseResult<List<StudyTask>> listByPlan(@RequestParam String planId) {
        List<StudyTask> list = studyTaskService.lambdaQuery()
                .eq(StudyTask::getPlanId, planId)
                .orderByAsc(StudyTask::getTaskOrder)
                .list();
        return ResponseResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "任务详情")
    public ResponseResult<StudyTask> getById(@PathVariable String id) {
        return ResponseResult.success(studyTaskService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新任务")
    public ResponseResult<Void> update(@PathVariable String id, @RequestBody StudyTask task) {
        task.setId(id);
        studyTaskService.updateById(task);
        return ResponseResult.success();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成任务")
    public ResponseResult<Void> complete(@PathVariable String id, @RequestParam(required = false) BigDecimal score) {
        StudyTask task = new StudyTask();
        task.setId(id);
        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        if (score != null) {
            task.setScore(score);
        }
        studyTaskService.updateById(task);
        return ResponseResult.success();
    }
}
