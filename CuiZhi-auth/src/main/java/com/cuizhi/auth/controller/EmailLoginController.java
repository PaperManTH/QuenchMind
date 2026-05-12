package com.cuizhi.auth.controller;

import com.cuizhi.auth.model.dto.EmailCodeLoginDto;
import com.cuizhi.auth.model.dto.EmailCodeSendDto;
import com.cuizhi.auth.security.EmailCodeAuthenticationToken;
import com.cuizhi.auth.service.EmailCodeService;
import com.cuizhi.core.common.CzHttpStatus;
import com.cuizhi.core.exception.CuiZhiException;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "邮箱登录", description = "邮箱验证码登录")
@Slf4j
@RestController
public class EmailLoginController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private EmailCodeService emailCodeService;

    public EmailLoginController(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/email/code")
    public ResponseResult<Void> sendCode(@RequestBody @Valid EmailCodeSendDto dto) {
        emailCodeService.sendLoginCode(dto.getEmail());
        return ResponseResult.success(null, "验证码已发送");
    }

    @Operation(summary = "邮箱验证码登录")
    @PostMapping("/login/email")
    public ResponseResult<Void> login(@RequestBody @Valid EmailCodeLoginDto dto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new EmailCodeAuthenticationToken(dto.getEmail(), dto.getCode())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 如果之前被 /oauth2/authorize 之类的请求拦截过，登录成功后跳回去
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest != null) {
            try {
                response.sendRedirect(savedRequest.getRedirectUrl());
            } catch (IOException e) {
                throw new CuiZhiException(CzHttpStatus.INTERNAL_ERROR.getCode(), "跳转失败", e);
            }
        }
        return ResponseResult.success(null, "登录成功");
    }
}
