package com.cuizhi.auth.controller;

import com.cuizhi.auth.service.WechatLoginService;
import com.cuizhi.auth.service.WechatStateService;
import com.cuizhi.core.common.CzHttpStatus;
import com.cuizhi.core.exception.CuiZhiException;
import com.cuizhi.core.model.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "微信登录", description = "微信开放平台扫码登录")
@Slf4j
@RestController("/login")
public class WechatLoginController {

    @Autowired
    private WechatStateService wechatStateService;

    @Autowired
    private WechatLoginService wechatLoginService;

    @Operation(summary = "发起微信扫码登录")
    @GetMapping("/wechat")
    public void start(
            @RequestParam(name = "redirect", required = false) String redirect,
            HttpServletResponse response
    ) throws IOException {
        try {
            String state = wechatStateService.createState(redirect);
            String url = wechatLoginService.buildAuthorizeUrl(state);
            response.sendRedirect(url);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "微信回调")
    @GetMapping("/wechat/callback")
    public ResponseResult<Void> callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String redirect = wechatStateService.consumeRedirect(state);
        if (StringUtils.isBlank(redirect)) {
            return ResponseResult.error(CzHttpStatus.UNAUTHORIZED.getCode(), "state 无效或已过期");
        }

        wechatLoginService.handleCallbackAndLogin(code);
        try {
            response.sendRedirect(redirect);
        } catch (IOException e) {
            throw new CuiZhiException(CzHttpStatus.INTERNAL_ERROR.getCode(), "跳转失败", e);
        }
        return ResponseResult.success(null, "登录成功");
    }
}
