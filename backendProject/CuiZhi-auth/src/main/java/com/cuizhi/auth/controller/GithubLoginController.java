package com.cuizhi.auth.controller;

import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.auth.service.GithubLoginService;
import com.cuizhi.core.common.RedisConstant;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * GitHub OAuth2 登录控制器
 */
@Tag(name = "GitHub登录", description = "GitHub OAuth2 登录")
@Slf4j
@RestController
@RequestMapping("/github")
@AllArgsConstructor
public class GithubLoginController {

    private final GithubLoginService githubLoginService;
    private final StringRedisTemplate redisTemplate;

    @Operation(summary = "发起 GitHub 登录")
    @GetMapping("/login")
    public void login(
            @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect,
            HttpServletResponse response) throws IOException {
        String state = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(
                RedisConstant.OAUTH2_STATE_KEY + state,
                StringUtils.hasText(redirect) ? redirect : "/",
                300, TimeUnit.SECONDS);

        String url = githubLoginService.buildAuthorizeUrl(state);
        response.sendRedirect(url);
    }

    @Operation(summary = "GitHub 回调")
    @GetMapping("/login/callback")
    public void callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response) throws IOException {

        String redirect = redisTemplate.opsForValue().get(RedisConstant.OAUTH2_STATE_KEY + state);
        if (!StringUtils.hasText(redirect)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "state 无效或已过期");
            return;
        }
        redisTemplate.delete(RedisConstant.OAUTH2_STATE_KEY + state);

        try {
            AuthResultDto tokens = githubLoginService.handleCallback(code);
            String redirectUrl = redirect + (redirect.contains("?") ? "&" : "?")
                    + "accessToken=" + tokens.getAccessToken()
                    + "&refreshToken=" + tokens.getRefreshToken()
                    + "&tokenType=" + tokens.getTokenType()
                    + "&expiresIn=" + tokens.getExpiresIn()
                    + "&refreshExpiresIn=" + tokens.getRefreshExpiresIn()
                    + "&userId=" + (tokens.getUserId() != null ? tokens.getUserId() : "")
                    + "&username=" + (tokens.getUsername() != null ? tokens.getUsername() : "")
                    + "&email=" + (tokens.getEmail() != null ? tokens.getEmail() : "")
                    + "&nickName=" + (tokens.getNickName() != null ? tokens.getNickName() : "")
                    + "&avatar=" + (tokens.getAvatar() != null ? tokens.getAvatar() : "");
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("GitHub 登录失败: {}", e.getMessage());
            response.sendRedirect(redirect + (redirect.contains("?") ? "&" : "?") + "error=" + e.getMessage());
        }
    }

    @Operation(summary = "GitHub 回调（JSON 模式，供桌面端使用）")
    @PostMapping("/login/exchange")
    public ResponseResult<AuthResultDto> exchange(
            @RequestBody Map<String, String> body,
            HttpServletResponse response) {

        String code = body.get("code");
        String state = body.get("state");

        if (!StringUtils.hasText(code) || !StringUtils.hasText(state)) {
            return ResponseResult.error("code 和 state 不能为空");
        }

        String redirect = redisTemplate.opsForValue().get(RedisConstant.OAUTH2_STATE_KEY + state);
        if (!StringUtils.hasText(redirect)) {
            return ResponseResult.error("state 无效或已过期");
        }
        redisTemplate.delete(RedisConstant.OAUTH2_STATE_KEY + state);

        try {
            AuthResultDto tokens = githubLoginService.handleCallback(code);
            return ResponseResult.success(tokens, "登录成功");
        } catch (Exception e) {
            log.error("GitHub 登录失败: {}", e.getMessage());
            return ResponseResult.error("GitHub 登录失败: " + e.getMessage());
        }
    }
}
