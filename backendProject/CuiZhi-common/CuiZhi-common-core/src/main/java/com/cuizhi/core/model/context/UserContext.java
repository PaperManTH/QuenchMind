package com.cuizhi.core.model.context;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author thpaperman
 * @Description 用户上下文（ThreadLocal，由 JwtAuthFilter 自动填充）
 *              不依赖 Spring Security，所有微服务共享同一套逻辑
 * @Date 2026/7/7
 * @Version 2.0
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public static void set(UserInfo user) {
        HOLDER.set(user);
    }

    public static UserInfo get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static String getUserId() {
        UserInfo user = get();
        return user != null ? user.getUserId() : null;
    }

    public static String getUserName() {
        UserInfo user = get();
        return user != null ? user.getUserName() : null;
    }

    // ---- 兼容旧调用名 ----

    public static UserInfo getCurrentUser() {
        return get();
    }

    public static String getCurrentUserId() {
        return getUserId();
    }

    public static String getCurrentUserName() {
        return getUserName();
    }

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
        private String userName;
    }
}
