package com.cuizhi.learning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.learning.service.StudyPlanResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学习计划-资料关联表（多对多） 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("studyPlanResource")
public class StudyPlanResourceController {

    @Autowired
    private StudyPlanResourceService  studyPlanResourceService;
}
