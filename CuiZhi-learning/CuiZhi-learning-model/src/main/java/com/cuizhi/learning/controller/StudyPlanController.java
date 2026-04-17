package com.cuizhi.learning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.learning.service.StudyPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习计划表 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("studyPlan")
public class StudyPlanController {

    @Autowired
    private StudyPlanService  studyPlanService;
}
