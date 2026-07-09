package com.cuizhi.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册DTO
 */
@Data
@Schema(description = "用户注册DTO")
public class RegisterDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 32, message = "用户名长度2-32位")
    @Schema(description = "用户名")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度6-64位")
    @Schema(description = "密码")
    private String password;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱（可选）")
    private String email;
}
