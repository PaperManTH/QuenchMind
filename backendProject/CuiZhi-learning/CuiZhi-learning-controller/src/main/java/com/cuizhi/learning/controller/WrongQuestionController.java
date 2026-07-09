package com.cuizhi.learning.controller;

import com.cuizhi.core.model.ResponseResult;
import com.cuizhi.learning.model.po.WrongQuestion;
import com.cuizhi.learning.service.WrongQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 错题控制器 — CRUD + 间隔复习
 */
@Slf4j
@RestController
@RequestMapping("/learning/wrong-question")
@Tag(name = "错题本", description = "错题 CRUD + 间隔复习")
@AllArgsConstructor
public class WrongQuestionController {

    private final WrongQuestionService wrongQuestionService;

    @PostMapping
    @Operation(summary = "添加错题")
    public ResponseResult<WrongQuestion> add(@RequestBody WrongQuestion question) {
        question.setReviewCount(0);
        question.setNextReviewAt(LocalDateTime.now().plusDays(1));
        wrongQuestionService.save(question);
        return ResponseResult.success(question);
    }

    @GetMapping("/list")
    @Operation(summary = "任务的错题列表")
    public ResponseResult<List<WrongQuestion>> listByTask(@RequestParam String taskId) {
        List<WrongQuestion> list = wrongQuestionService.lambdaQuery()
                .eq(WrongQuestion::getTaskId, taskId)
                .orderByDesc(WrongQuestion::getCreatedAt)
                .list();
        return ResponseResult.success(list);
    }

    @GetMapping("/review")
    @Operation(summary = "获取待复习错题")
    public ResponseResult<List<WrongQuestion>> listForReview(@RequestParam String userId) {
        List<WrongQuestion> list = wrongQuestionService.lambdaQuery()
                .le(WrongQuestion::getNextReviewAt, LocalDateTime.now())
                .orderByAsc(WrongQuestion::getNextReviewAt)
                .list();
        return ResponseResult.success(list);
    }

    @PutMapping("/{id}/review")
    @Operation(summary = "复习错题（更新间隔）")
    public ResponseResult<Void> review(@PathVariable String id, @RequestParam boolean correct) {
        WrongQuestion question = wrongQuestionService.getById(id);
        if (question == null) {
            return ResponseResult.error("错题不存在");
        }

        int reviewCount = question.getReviewCount() != null ? question.getReviewCount() + 1 : 1;
        int nextDays = correct ? (int) Math.pow(2, reviewCount) : 1;

        WrongQuestion update = new WrongQuestion();
        update.setId(id);
        update.setReviewCount(reviewCount);
        update.setIsCorrect(correct ? 1 : 0);
        update.setNextReviewAt(LocalDateTime.now().plusDays(nextDays));
        wrongQuestionService.updateById(update);
        return ResponseResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除错题")
    public ResponseResult<Void> delete(@PathVariable String id) {
        wrongQuestionService.removeById(id);
        return ResponseResult.success();
    }
}
