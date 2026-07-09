package com.cuizhi.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.ToString;

/**
 * @Author thpaperman
 * @Description 分页查询通用参数
 * @Date 2026/3/2
 * @DAY_NAME_FULL: 星期一
 * @Version 1.0
 */
@Tag(name = "分页查询通用参数")
@Data
@ToString
public class PageParams {

    /**
     * 当前页码
     */
    @Schema(description = "页码")
    private long pageNo = 1L;

    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数")
    private long pageSize = 20L;

    public PageParams() {
    }

    public PageParams(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}