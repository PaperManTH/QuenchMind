package com.cuizhi.auth.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 认证用户请求参数
 * @Version: 1.0
 */
@Data
@Schema(description = "认证用户请求参数")
public class AuthParamsDto {

    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "手机号", example = "13800138000")
    private String cellphone;

    @Schema(description = "验证码", example = "ABCD")
    @JSONField(name = "checkcode")
    private String checkCode;

    @Schema(description = "验证码key", example = "uuid-key")
    @JSONField(name = "checkcodekey")
    private String checkCodeKey;

    @Schema(description = "认证方式", example = "PASSWORD", allowableValues = {"PASSWORD", "PHONE", "EMAIL", "WX"})
    private String authType;

    @Schema(description = "认证信息扩展数据")
    private Map<String, Object> payload = new HashMap<>();
}
