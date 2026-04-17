package com.cuizhi.auth.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @version 1.0
 * @description 认证用户请求参数
 * @date 2022/9/29 10:56
 */
@Data
public class AuthParamsDto {

    /** 用户名 **/
    private String username;

    /** 密码 **/
    private String password;

    /** 手机号 **/
    private String cellphone;

    /** 验证码 **/
    @JSONField(name = "checkcode")
    private String checkCode;

    /** 验证码key **/
    @JSONField(name = "checkcodekey")
    private String checkCodeKey;

    /** 认证方式 **/
    private String authType;

    /** 认证信息扩展数据 **/
    private Map<String, Object> payload = new HashMap<>();
}
