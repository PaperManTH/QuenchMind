package com.cuizhi.core.common;

/**
 * @Author thpaperman
 * @Description Redis 常量类（统一所有模块 Redis key）
 * @Date 2026/4/6
 * @Version 1.1
 */
public class RedisConstant {

    /** Redis key 统一前缀 */
    public static final String REDIS_KEY_PREFIX = "quenchMind:";

    // ---- 认证相关 ----

    public static final String CAPTCHA_KEY = REDIS_KEY_PREFIX + "captcha:";
    public static final String EMAIL_CODE_KEY = REDIS_KEY_PREFIX + "email-code:";
    public static final String OAUTH2_STATE_KEY = REDIS_KEY_PREFIX + "oauth2-state:";
    public static final String REFRESH_TOKEN = REDIS_KEY_PREFIX + "refresh-token:";

    // ---- 资源上传 ----

    /** 分片上传进度 key（Set: 已传分片序号） */
    public static final String RESOURCE_UPLOAD_KEY = REDIS_KEY_PREFIX + "resource:upload:";

    /** 文件 MD5 → resourceId 秒传映射 key */
    public static final String RESOURCE_FILE_KEY = REDIS_KEY_PREFIX + "resource:file:";

    /** 上传进度过期时间（小时） */
    public static final long RESOURCE_UPLOAD_EXPIRE_HOURS = 24;

    // ---- Token 有效期 ----

    /** 访问令牌有效期（毫秒） */
    public static final long ACCESS_TOKEN_EXPIRATION = 30L * 24 * 60 * 60 * 1000;

    /** 刷新令牌有效期（毫秒） */
    public static final long REFRESH_TOKEN_EXPIRATION = 30L * 24 * 60 * 60 * 1000;

}
