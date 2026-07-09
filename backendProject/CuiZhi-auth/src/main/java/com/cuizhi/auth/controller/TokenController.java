package com.cuizhi.auth.controller;

import com.cuizhi.auth.model.dto.AuthParamsDto;
import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.auth.utils.JwtUtils;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.cuizhi.core.common.RedisConstant.REFRESH_TOKEN;
import static com.cuizhi.core.common.RedisConstant.REFRESH_TOKEN_EXPIRATION;

/**
 * Token 管理
 */
@Tag(name = "Token 管理", description = "Token 刷新和管理")
@Slf4j
@RestController
@AllArgsConstructor
public class TokenController {

    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    @Operation(summary = "刷新 Token")
    @PostMapping("/token/refresh")
    public ResponseResult<AuthResultDto> refreshToken(@RequestBody AuthParamsDto paramsDto) {
        try {
            String refreshToken = paramsDto.getPayload().get("refreshToken").toString();
            String userId = jwtUtils.getUserIdFromToken(refreshToken);
            String refreshKey = REFRESH_TOKEN + userId;
            String storedToken = redisTemplate.opsForValue().get(refreshKey);

            if (storedToken == null || !storedToken.equals(refreshToken)) {
                return ResponseResult.error("Refresh Token 无效或已过期");
            }

            AuthResultDto newTokens = jwtUtils.refreshToken(refreshToken);
            redisTemplate.opsForValue().set(refreshKey, newTokens.getRefreshToken(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

            return ResponseResult.success(newTokens, "Token 刷新成功");
        } catch (Exception e) {
            log.error("Token 刷新失败: {}", e.getMessage());
            return ResponseResult.error("Token 刷新失败");
        }
    }

    @Operation(summary = "登出")
    @PostMapping("/token/logout")
    public ResponseResult<Void> logout(@RequestBody AuthParamsDto paramsDto) {
        try {
            String refreshToken = paramsDto.getPayload().get("refreshToken").toString();
            String userId = jwtUtils.getUserIdFromToken(refreshToken);
            redisTemplate.delete(REFRESH_TOKEN + userId);
            return ResponseResult.success(null, "登出成功");
        } catch (Exception e) {
            return ResponseResult.error("登出失败");
        }
    }
}
