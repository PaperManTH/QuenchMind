package com.cuizhi.auth.controller;

import com.cuizhi.auth.model.dto.CaptchaParamsDto;
import com.cuizhi.auth.model.dto.CaptchaResultDto;
import com.cuizhi.auth.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码服务接口
 */
@Tag(name = "验证码服务", description = "验证码服务接口")
@Slf4j
@RestController
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @Operation(summary = "生成验证码")
    @PostMapping("/captcha")
    public CaptchaResultDto generateCaptcha(@RequestBody @Validated CaptchaParamsDto paramsDto) {
        return captchaService.generate(paramsDto);
    }
}

