package com.cuizhi.auth.model.dto;


import lombok.Data;

/**
 * @author Mr.M
 * @version 1.0
 * @description 验证码生成结果类
 * @date 2022/9/29 15:48
 */
@Data
public class CaptchaResultDto {

    /**
     * key用于验证
     */
    private String key;

    /**
     * 统一返回验证码
     * 举例：
     * 1.图片验证码为:图片base64编码
     * 2.短信验证码为:null
     * 3.邮件验证码为: null
     */
    private String validateCode;
}
