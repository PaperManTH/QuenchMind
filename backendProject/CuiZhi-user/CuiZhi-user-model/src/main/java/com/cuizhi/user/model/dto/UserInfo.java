package com.cuizhi.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 用户信息 DTO（用于用户信息展示/修改）。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户信息")
public class UserInfo {

    @Schema(description = "用户ID", example = "$dam21ronlk3214hbj2kb5ka9012")
    private String userId;

    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Schema(description = "手机号", example = "13800138000")
    private String userPhone;

    @Schema(description = "邮箱", example = "user@example.com")
    private String userEmail;

    @Schema(description = "密码哈希（仅内部使用，不建议对外返回）", example = "******")
    private String passwordHash;

    @Schema(description = "登录方式", example = "PASSWORD", allowableValues = {"PASSWORD", "WX", "PHONE", "EMAIL"})
    private String loginType;

    @Schema(description = "昵称", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    @Schema(description = "性别", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    private String userGender;

    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    @Schema(description = "状态", example = "1", allowableValues = {"1", "0"})
    private Integer status;
}

