package com.cuizhi.auth.service.impl;

import com.cuizhi.auth.service.WechatStateService;
import com.cuizhi.core.common.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class WechatStateServiceImpl implements WechatStateService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String createState(String redirect) {
        String state = UUID.randomUUID().toString().replace("-", "");
        String key = RedisConstant.WECHAT_STATE_KEY_SUFFIX + state;
        String value = StringUtils.defaultIfBlank(redirect, "/");
        redisTemplate.opsForValue().set(key, value, 300, TimeUnit.SECONDS);
        return state;
    }

    @Override
    public String consumeRedirect(String state) {
        if (StringUtils.isBlank(state)) {
            return null;
        }
        String key = RedisConstant.WECHAT_STATE_KEY_SUFFIX + state;
        String redirect = redisTemplate.opsForValue().get(key);
        if (redirect != null) {
            redisTemplate.delete(key);
        }
        return redirect;
    }
}

