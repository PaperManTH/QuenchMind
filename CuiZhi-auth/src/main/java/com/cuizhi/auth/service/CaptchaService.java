package com.cuizhi.auth.service;

import com.cuizhi.auth.model.dto.CaptchaParamsDto;
import com.cuizhi.auth.model.dto.CaptchaResultDto;

/**
 * @Author thpaperman
 * @Description 验证码服务接口
 * @Date 2026/4/6
 * @Version 1.0
 */
public interface CaptchaService {

    /**
     * 生成验证码
     * @param paramsDto 验证码参数
     * @return 验证码结果
     */
    CaptchaResultDto generate(CaptchaParamsDto paramsDto);

    /**
     * 校验验证码
     * @param checkCodeKey 验证码 key
     * @param checkCode 验证码
     * @return 校验结果
     */
    Boolean verifyCode(String checkCodeKey, String checkCode);
}
