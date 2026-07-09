package com.cuizhi.user.model.vo;

import com.cuizhi.user.model.po.CzGroupMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 学习小组详情信息。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "学习小组详情信息")
public class GroupDetailsVO {

    @Schema(description = "小组ID")
    private String id;

    @Schema(description = "小组名称")
    private String groupName;

    @Schema(description = "小组描述")
    private String description;

    @Schema(description = "小组创建者ID")
    private String ownerId;

    @Schema(description = "小组最大成员数")
    private Integer maxMembers;

    @Schema(description = "小组状态")
    private Short status;

    @Schema(description = "小组成员列表")
    private List<CzGroupMember> members;

    @Schema(description = "当前用户在小组中的角色（OWNER/ADMIN/MEMBER/null）")
    private String currentUserRole;

    @Schema(description = "当前用户是否是小组成员")
    private Boolean isMember;
}

