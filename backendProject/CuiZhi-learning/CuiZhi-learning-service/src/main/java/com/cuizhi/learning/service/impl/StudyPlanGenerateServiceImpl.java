package com.cuizhi.learning.service.impl;

import com.cuizhi.learning.client.LearningAiClient;
import com.cuizhi.learning.mapper.StudyPlanMapper;
import com.cuizhi.learning.mapper.StudyTaskMapper;
import com.cuizhi.learning.model.dto.GenerateRequest;
import com.cuizhi.learning.model.dto.GenerateResult;
import com.cuizhi.learning.model.po.StudyPlan;
import com.cuizhi.learning.model.po.StudyTask;
import com.cuizhi.learning.service.StudyPlanGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author thpaperman
 * @Description AI 学习计划生成 — 调 Python AI 引擎生成并入库
 * @Date 2026/7/7
 * @Version 1.0
 */
@Slf4j
@Service
public class StudyPlanGenerateServiceImpl implements StudyPlanGenerateService {

    private final LearningAiClient aiClient;
    private final StudyPlanMapper planMapper;
    private final StudyTaskMapper taskMapper;

    public StudyPlanGenerateServiceImpl(LearningAiClient aiClient,
                                         StudyPlanMapper planMapper,
                                         StudyTaskMapper taskMapper) {
        this.aiClient = aiClient;
        this.planMapper = planMapper;
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenerateResult generate(GenerateRequest req) {
        // 调 Python AI 生成任务列表
        List<Map<String, Object>> tasks = aiClient.generateStudyPlan(
                req.getGoal(), req.getDays(), req.getDailyMinutes());

        // 创建学习计划
        StudyPlan plan = new StudyPlan();
        plan.setId(UUID.randomUUID().toString().replace("-", ""));
        plan.setUserId(req.getUserId());
        plan.setTitle(req.getGoal());
        plan.setGoal(req.getGoal());
        plan.setStartDate(LocalDate.now());
        plan.setEndDate(LocalDate.now().plusDays(req.getDays()));
        plan.setTotalDays(req.getDays());
        plan.setDailyMinutes(req.getDailyMinutes());
        plan.setProgress(BigDecimal.ZERO);
        plan.setStatus("ACTIVE");
        plan.setAiModel("langchain");
        plan.setCreatedAt(LocalDateTime.now());
        planMapper.insert(plan);

        // 批量创建学习任务
        if (tasks != null) {
            int order = 1;
            for (Map<String, Object> taskMap : tasks) {
                StudyTask task = new StudyTask();
                task.setPlanId(plan.getId());
                task.setTitle((String) taskMap.getOrDefault("title", ""));
                task.setContent((String) taskMap.getOrDefault("content", ""));
                task.setTaskOrder(order++);
                task.setTaskType((String) taskMap.getOrDefault("type", "阅读"));
                task.setPriority(2);
                task.setStatus("PENDING");
                task.setCreatedAt(LocalDateTime.now());
                taskMapper.insert(task);
            }
        }

        GenerateResult result = new GenerateResult();
        result.setPlanId(plan.getId());
        result.setTitle(req.getGoal());
        result.setTasks(tasks);
        return result;
    }
}
