package com.cuizhi.user.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuizhi.user.model.enums.GroupMemberRole;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: thpaperman
 * @Date: 2026/5/13
 * @Description: 小组成员表
 * @Version: 1.0
 */
@Data
@Builder
@TableName("cz_group_member")
public class CzGroupMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 小组ID
     */
    private String groupId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色 OWNER所有者 ADMIN管理员 MEMBER普通成员
     */
    private GroupMemberRole role;

    /**
     * 加入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinedAt;
}
