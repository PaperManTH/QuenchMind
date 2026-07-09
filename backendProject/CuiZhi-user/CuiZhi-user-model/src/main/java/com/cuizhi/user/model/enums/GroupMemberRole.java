package com.cuizhi.user.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: thpaperman
 * @Date: 2026/5/15
 * @Description: 小组成员角色枚举
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum GroupMemberRole {
    /**
     * 所有者（创建者）- 最高权限
     */
    OWNER("OWNER", "所有者", 3),

    /**
     * 管理员 - 管理权限
     */
    ADMIN("ADMIN", "管理员", 2),

    /**
     * 普通成员 - 基础权限
     */
    MEMBER("MEMBER", "普通成员", 1);

    /**
     * 角色代码（存入数据库的值）
     */
    private final String code;

    /**
     * 角色名称（展示用）
     */
    private final String name;

    /**
     * 权限等级
     */
    private final int level;

    /**
     * 根据代码获取枚举
     * @param code 角色代码
     * @return 对应的枚举值
     * @throws IllegalArgumentException 如果代码无效
     */
    public static GroupMemberRole fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (GroupMemberRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("无效的角色代码: " + code);
    }

    /**
     * 是否可以修改小组信息
     */
    public boolean canUpdateGroup() {
        return this.level >= ADMIN.level;
    }

    /**
     * 是否可以移除成员
     */
    public boolean canRemoveMember() {
        return this.level >= ADMIN.level;
    }

    /**
     * 是否可以添加成员
     */
    public boolean canAddMember() {
        return this.level >= ADMIN.level;
    }

    /**
     * 是否可以解散小组
     */
    public boolean canDisbandGroup() {
        return this == OWNER;
    }

    /**
     * 是否可以转让所有权
     */
    public boolean canTransferOwnership() {
        return this == OWNER;
    }

    /**
     * 是否可以授权其他成员角色
     */
    public boolean canPromoteMember() {
        return this == OWNER;
    }

    /**
     * 是否有权限执行某操作
     * @param requiredRole 所需的最低角色
     * @return true 如果有权限
     */
    public boolean hasPermission(GroupMemberRole requiredRole) {
        return this.level >= requiredRole.level;
    }
}
