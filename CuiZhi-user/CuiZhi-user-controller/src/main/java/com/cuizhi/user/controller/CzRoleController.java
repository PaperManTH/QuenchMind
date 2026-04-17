package com.cuizhi.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cuizhi.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author thpaperman
 */
@Slf4j
@RestController
@RequestMapping("role")
public class CzRoleController {

    @Autowired
    private RoleService  roleService;
}
