package com.cuizhi.core.model.context;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/4 13:35
 * @Description: 用户上下文信息
 * @Version: 1.0
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     */
    public static void setCurrentUser(UserInfo userInfo) {
        USER_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取当前用户信息
     */
    public static UserInfo getCurrentUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * 清除当前用户信息（防止内存泄漏）
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }

    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long userId;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 手机号
         */
        private String userPhone;

        /**
         * 邮箱
         */
        private String userEmail;

        /**
         * 登录方式 PASSWORD/WX/PHONE/EMAIL
         */
        private String loginType;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 头像
         */
        private String userAvatar;

        /**
         * 性别
         */
        private String userGender;

        /**
         * 生日
         */
        private LocalDate birthday;

        /**
         * 状态 1正常 0禁用
         */
        private Integer status;

        /**
         * 最后登录时间
         */
        private LocalDateTime lastLoginAt;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private LocalDateTime deletedAt;
    }
}