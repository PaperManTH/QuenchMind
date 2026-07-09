package com.cuizhi.auth.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户账号表
 * </p>
 *
 * @author thpaperman
 */
@Data
@TableName("cz_user")
public class CzUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

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
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 登录方式 PASSWORD/WX/PHONE/EMAIL/GITHUB
     */
    private String loginType;

    /**
     * OAuth 平台（GITHUB等）
     */
    private String oauthProvider;

    /**
     * OAuth 平台用户ID
     */
    private String oauthProviderId;

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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(fill = FieldFill.UPDATE)
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;


}
