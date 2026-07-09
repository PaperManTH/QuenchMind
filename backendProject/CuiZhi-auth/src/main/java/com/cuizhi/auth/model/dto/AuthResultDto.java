package com.cuizhi.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 认证结果信息
 * @Version: 1.0
 */
@Data
@ToString
@Builder
@Schema(description = "认证结果信息")
public class AuthResultDto {

    @Schema(description = "JWT 令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "令牌有效期（秒）", example = "3600")
    private Long expiresIn;

    @Schema(description = "刷新令牌有效期（秒）", example = "86400")
    private Long refreshExpiresIn;

    // ── 用户信息 ──

    @Schema(description = "用户 ID")
    private String userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像 URL")
    private String avatar;

}
