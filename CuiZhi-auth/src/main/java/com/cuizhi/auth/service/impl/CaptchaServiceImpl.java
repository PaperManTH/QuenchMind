package com.cuizhi.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.cuizhi.auth.model.dto.CaptchaParamsDto;
import com.cuizhi.auth.model.dto.CaptchaResultDto;
import com.cuizhi.auth.service.CaptchaService;
import com.cuizhi.core.common.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author thpaperman
 * @Description 验证码服务接口实现类
 * @Date 2026/4/6
 * @Version 1.0
 */
@Slf4j
@Service("CaptchaService")
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${captcha.timeout}")
    private Integer timeout;

    @Value("${captcha.size}")
    private Integer size;

    @Value("${captcha.width}")
    private Integer width;

    @Value("${captcha.height}")
    private Integer height;

    /**
     * 生成验证码
     * @param paramsDto 验证码参数
     * @return 验证码结果
     */
    @Override
    public CaptchaResultDto generate(CaptchaParamsDto paramsDto) {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height, size, 5);

        String code = captcha.getCode();
        String key = IdUtil.simpleUUID();
        // 缓存验证码
        redisTemplate.opsForValue().set(RedisConstant.CAPTCHA_KEY_SUFFIX + key, code, timeout, TimeUnit.SECONDS);

        String base64Image = captcha.getImageBase64Data();

        CaptchaResultDto resultDto = new CaptchaResultDto();
        resultDto.setKey(key);
        resultDto.setValidateCode(base64Image);

        log.debug("生成图片验证码，key: {}, code: {}", key, code);
        return resultDto;
    }

    @Override
    public Boolean verifyCode(String checkCodeKey, String checkCode) {
        if (checkCodeKey == null || checkCodeKey.isEmpty() || checkCode == null || checkCode.isEmpty()) {
            log.warn("验证码 key 或验证码不能为空");
            return false;
        }

        String storedCode = redisTemplate.opsForValue().get(RedisConstant.CAPTCHA_KEY_SUFFIX + checkCodeKey);

        if (storedCode == null) {
            log.warn("验证码已过期或不存在，key: {}", checkCodeKey);
            return false;
        }

        boolean result = storedCode.equalsIgnoreCase(checkCode);

        if (result) {
            redisTemplate.delete(RedisConstant.CAPTCHA_KEY_SUFFIX + checkCodeKey);
            log.debug("验证码校验成功，key: {}", checkCodeKey);
        } else {
            log.warn("验证码校验失败，期望: {}, 实际: {}", storedCode, checkCode);
        }
        return result;
    }

}
