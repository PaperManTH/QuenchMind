package com.cuizhi.learning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.learning.service.WrongQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 错题复习表 前端控制器
 * </p>
 *
 * @author cuizhi
 */
@Slf4j
@RestController
@RequestMapping("wrongQuestion")
public class WrongQuestionController {

    @Autowired
    private WrongQuestionService  wrongQuestionService;
}
