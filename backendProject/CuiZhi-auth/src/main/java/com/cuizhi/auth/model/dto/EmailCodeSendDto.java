package com.cuizhi.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 发送邮箱验证码参数
 * @Version: 1.0
 */
@Data
@Schema(description = "发送邮箱验证码参数")
public class EmailCodeSendDto {

    @Schema(description = "邮箱地址", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;
}

