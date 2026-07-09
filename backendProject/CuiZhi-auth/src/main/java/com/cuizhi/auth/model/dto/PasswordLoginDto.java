package com.cuizhi.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 密码登录DTO
 */
@Data
@Schema(description = "密码登录DTO")
public class PasswordLoginDto {

    @NotBlank(message = "用户名/手机号不能为空")
    @Schema(description = "用户名或手机号")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
