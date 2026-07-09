package com.cuizhi.user.model.dto;

import com.cuizhi.core.model.validation.group.Insert;
import com.cuizhi.core.model.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学习小组 DTO。
 */
@Data
@Schema(description = "学习小组 DTO")
public class StudyGroupDTO {

    @Schema(description = "小组ID", example = "b6e75f45-8e3e-4f5e-9d29-4c2aa34a7b9d")
    @NotBlank(message = "小组ID不能为空", groups = {Update.class})
    private String groupId;

    @Schema(description = "小组名称", example = "Java刷题小组")
    @NotBlank(message = "小组名称不能为空", groups = {Insert.class, Update.class})
    private String groupName;

    @Schema(description = "小组描述", example = "一起学习，一起进步")
    @NotBlank(message = "小组描述不能为空", groups = {Insert.class, Update.class})
    private String description;

    @Schema(description = "小组最大成员数", example = "10")
    @NotNull(message = "小组最大成员数不能为空", groups = {Insert.class, Update.class})
    @Min(value = 2, message = "小组最大成员数必须大于等于2", groups = {Insert.class, Update.class})
    private Integer maxMembers;
}

