package com.cuizhi.resource.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 学习资料表
 * </p>
 *
 * @author cuizhi
 */
@Data
@TableName("cz_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 上传用户ID
     */
    private String userId;

    /**
     * 资料标题
     */
    private String title;

    /**
     * 资料描述
     */
    private String description;

    /**
     * 原文件名
     */
    private String fileName;

    /**
     * 文件类型 pdf/ppt/docx
     */
    private String fileType;

    /**
     * 文件后缀
     */
    private String fileExt;

    /**
     * 文件大小，字节
     */
    private Long fileSize;

    /**
     * 文件MD5
     */
    private String fileMd5;

    /**
     * 存储桶
     */
    private String storageBucket;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 解析错误信息
     */
    private String parseErrorMsg;

    /**
     * 解析状态 0未解析 1解析中 2成功 3失败
     */
    private Integer parseStatus;

    /**
     * 向量化状态 0未处理 1处理中 2成功 3失败
     */
    private Integer vectorStatus;

    /**
     * 页数
     */
    private Integer pageCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


}
