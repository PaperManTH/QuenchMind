package com.cuizhi.auth.service.impl;

import com.cuizhi.auth.service.EmailCodeService;
import com.cuizhi.core.common.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailCodeServiceImpl implements EmailCodeService {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired(required = false)
    @Nullable
    private JavaMailSender javaMailSender;

    @Value("${email.code.timeout:300}")
    private int timeoutSeconds;

    @Value("${email.code.length:6}")
    private int codeLength;

    @Value("${spring.mail.username:}")
    private String from;

    @Override
    public void sendLoginCode(String email) {
        String code = generateCode(codeLength);
        String key = RedisConstant.EMAIL_CODE_KEY_SUFFIX + email.toLowerCase();
        redisTemplate.opsForValue().set(key, code, timeoutSeconds, TimeUnit.SECONDS);

        if (javaMailSender == null) {
            log.warn("JavaMailSender 未配置，邮箱验证码仅写入 Redis。email={}, code={}", email, code);
            return;
        }
        if (StringUtils.isBlank(from)) {
            log.warn("spring.mail.username 未配置，无法发信。email={}, code={}", email, code);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("QuenchMind 登录验证码");
        message.setText("你的登录验证码是: " + code + "，" + timeoutSeconds + " 秒内有效。");
        javaMailSender.send(message);
    }

    @Override
    public boolean verifyLoginCode(String email, String code) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            return false;
        }
        String key = RedisConstant.EMAIL_CODE_KEY_SUFFIX + email.toLowerCase();
        String stored = redisTemplate.opsForValue().get(key);
        if (stored == null) {
            return false;
        }
        boolean ok = stored.equalsIgnoreCase(code.trim());
        if (ok) {
            redisTemplate.delete(key);
        }
        return ok;
    }

    private static String generateCode(int len) {
        int length = Math.max(4, Math.min(len, 8));
        int bound = (int) Math.pow(10, length);
        int value = RANDOM.nextInt(bound);
        return String.format("%0" + length + "d", value);
    }
}

