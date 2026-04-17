package com.cuizhi.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 用户账号表 前端控制器
 * </p>
 *
 * @author thpaperman
 */
@Slf4j
@RestController
@RequestMapping("user")
public class CzUserController {

    @Autowired
    private UserService  userService;
}
