package com.cuizhi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.auth.mapper.CzUserMapper;
import com.cuizhi.auth.model.dto.AuthResultDto;
import com.cuizhi.auth.model.dto.PasswordLoginDto;
import com.cuizhi.auth.model.dto.RegisterDto;
import com.cuizhi.auth.model.po.CzUser;
import com.cuizhi.auth.utils.JwtUtils;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.cuizhi.core.common.RedisConstant.*;

/**
 * 密码登录/注册
 */
@Tag(name = "账号密码", description = "用户名密码登录/注册")
@Slf4j
@RestController
@RequestMapping("/password")
@AllArgsConstructor
public class PasswordLoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CzUserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseResult<Void> register(@RequestBody @Valid RegisterDto dto) {
        if (userMapper.selectCount(new LambdaQueryWrapper<CzUser>()
                .eq(CzUser::getUserName, dto.getUserName())) > 0) {
            return ResponseResult.error("用户名已被注册");
        }
        if (StringUtils.hasText(dto.getEmail()) && userMapper.selectCount(new LambdaQueryWrapper<CzUser>()
                .eq(CzUser::getUserEmail, dto.getEmail())) > 0) {
            return ResponseResult.error("邮箱已被注册");
        }

        CzUser user = new CzUser();
        user.setUserName(dto.getUserName());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setLoginType("PASSWORD");
        user.setNickName(dto.getUserName());
        user.setStatus(1);
        if (StringUtils.hasText(dto.getEmail())) {
            user.setUserEmail(dto.getEmail());
        }
        userMapper.insert(user);
        return ResponseResult.success(null, "注册成功");
    }

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public ResponseResult<AuthResultDto> login(@RequestBody PasswordLoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getAccount(), dto.getPassword()));

            String username = authentication.getName();
            CzUser user = userMapper.selectOne(new LambdaQueryWrapper<CzUser>()
                    .eq(CzUser::getUserName, username)
                    .or()
                    .eq(CzUser::getUserPhone, username));
            if (user == null) {
                return ResponseResult.error("用户不存在");
            }

            AuthResultDto tokens = jwtUtils.generateTokens(user.getUserName(), user.getId());
            tokens.setUserId(user.getId());
            tokens.setUsername(user.getUserName());
            tokens.setEmail(user.getUserEmail());
            tokens.setNickName(user.getNickName());
            tokens.setAvatar(user.getUserAvatar());

            redisTemplate.opsForValue().set(
                    REFRESH_TOKEN + user.getId(),
                    tokens.getRefreshToken(),
                    REFRESH_TOKEN_EXPIRATION,
                    TimeUnit.MILLISECONDS);

            return ResponseResult.success(tokens, "登录成功");
        } catch (BadCredentialsException e) {
            return ResponseResult.error("账号或密码错误");
        } catch (DisabledException e) {
            return ResponseResult.error("账号已被禁用");
        } catch (UsernameNotFoundException e) {
            return ResponseResult.error("用户不存在");
        } catch (Exception e) {
            log.error("登录失败: {}", dto.getAccount(), e);
            return ResponseResult.error("登录失败，请稍后重试");
        }
    }
}
