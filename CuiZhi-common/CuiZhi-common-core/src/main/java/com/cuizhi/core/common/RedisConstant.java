package com.cuizhi.core.common;

/**
 * @Author thpaperman
 * @Description Redis 常量类
 * @Date 2026/4/6
 * @Version 1.0
 */
public class RedisConstant {

    /** Redis key 统一前缀 **/
    public static final String CAPTCHA_KEY_PREFIX = "quenchMind:";

    /** 验证码 key **/
    public static final String CAPTCHA_KEY_SUFFIX = CAPTCHA_KEY_PREFIX + "captcha:";

    public static final String EMAIL_CODE_KEY_SUFFIX = CAPTCHA_KEY_PREFIX + "email-code:";

    public static final String WECHAT_STATE_KEY_SUFFIX = CAPTCHA_KEY_PREFIX + "wechat-state:";

}
