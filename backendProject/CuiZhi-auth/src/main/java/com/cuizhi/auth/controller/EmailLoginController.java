package com.cuizhi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.auth.model.dto.EmailCodeLoginDto;
import com.cuizhi.auth.model.dto.EmailCodeSendDto;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.security.EmailCodeAuthenticationToken;
import com.cuizhi.auth.service.EmailCodeService;
import com.cuizhi.auth.utils.JwtUtils;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.cuizhi.core.common.RedisConstant.REFRESH_TOKEN;
import static com.cuizhi.core.common.RedisConstant.REFRESH_TOKEN_EXPIRATION;

/**
 * 邮箱登录 — 登录即注册
 */
@Tag(name = "邮箱登录", description = "邮箱验证码登录")
@Slf4j
@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailLoginController {

    private final AuthenticationManager authenticationManager;
    private final EmailCodeService emailCodeService;
    private final CzUserMapper czUserMapper;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/login/code")
    public ResponseResult<Void> sendCode(@RequestBody @Valid EmailCodeSendDto dto) {
        emailCodeService.sendLoginCode(dto.getEmail());
        return ResponseResult.success(null, "验证码已发送");
    }

    @Operation(summary = "邮箱验证码登录（登录即注册）")
    @PostMapping("/login")
    public ResponseResult<AuthResultDto> login(@RequestBody @Valid EmailCodeLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new EmailCodeAuthenticationToken(dto.getEmail(), dto.getCode()));

        CzUser user = czUserMapper.selectOne(
                new LambdaQueryWrapper<CzUser>().eq(CzUser::getUserEmail, dto.getEmail()));
        if (user == null) {
            return ResponseResult.error("登录失败");
        }

        AuthResultDto tokens = jwtUtils.generateTokens(user.getUserName(), user.getId());
        tokens.setUserId(user.getId());
        tokens.setUsername(user.getUserName());
        tokens.setEmail(user.getUserEmail());
        tokens.setNickName(user.getNickName());
        tokens.setAvatar(user.getUserAvatar());

        String refreshKey = REFRESH_TOKEN + user.getId();
        redisTemplate.opsForValue().set(refreshKey, tokens.getRefreshToken(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        return ResponseResult.success(tokens, "登录成功");
    }
}
