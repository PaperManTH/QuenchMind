package com.cuizhi.user.controller;

import com.cuizhi.core.annotation.CurrentUserId;
import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.core.model.validation.group.Insert;
import com.cuizhi.core.model.validation.group.Update;
import com.cuizhi.user.model.dto.StudyGroupDTO;
import com.cuizhi.user.model.po.CzStudyGroup;
import com.cuizhi.user.model.vo.GroupDetailsVO;
import com.cuizhi.user.service.StudyGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习小组接口
 */
@Tag(name = "学习小组接口", description = "学习小组接口")
@Slf4j
@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建学习小组")
    public ResponseResult<Void> createGroup(@RequestBody @Validated(Insert.class) StudyGroupDTO studyGroup,
                                            @CurrentUserId String userId) {
        studyGroupService.createGroup(studyGroup, userId);
        return ResponseResult.success();
    }

    @GetMapping("/list")
    @Operation(summary = "获取学习小组列表")
    public ResponseResult<List<CzStudyGroup>> listGroups(@CurrentUserId String userId) {
        List<CzStudyGroup> groups = studyGroupService.listGroups(userId);
        return ResponseResult.success(groups);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "获取学习小组详细信息")
    public ResponseResult<GroupDetailsVO> getGroupDetail(@PathVariable @NotBlank String groupId,
                                                         @CurrentUserId String userId) {
        GroupDetailsVO group = studyGroupService.getGroupDetail(groupId, userId);
        return ResponseResult.success(group);
    }

    @PostMapping("/updGroup")
    @Operation(summary = "更新学习小组信息")
    public ResponseResult<CzStudyGroup> updateGroup(@RequestBody @Validated(Update.class) StudyGroupDTO studyGroup,
                                                    @CurrentUserId String userId) {
        return ResponseResult.success(studyGroupService.updateGroup(studyGroup, userId));
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "解散学习小组")
    public ResponseResult<Void> disbandGroup(@PathVariable @NotBlank String groupId,
                                             @CurrentUserId String userId) {
        studyGroupService.disbandGroup(groupId, userId);
        return ResponseResult.success();
    }

}
