package com.cuizhi.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuizhi.core.common.RedisConstant;
import com.cuizhi.core.exception.resource.ResourceUploadException;
import com.cuizhi.resource.config.RabbitMqConfig;
import com.cuizhi.resource.mapper.ResourceMapper;
import com.cuizhi.resource.model.dto.UploadResult;
import com.cuizhi.resource.model.po.Resource;
import com.cuizhi.resource.service.ResourceUploadService;
import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author thpaperman
 * @Description 文件分片上传 + MinIO 存储
 *              路径：chunks/{date}/{md5_0}/{md5}/part_{n}
 *                    resources/{userId}/{date}/{md5_0}/{md5}_{fileName}
 * @Date 2026/7/7
 * @Version 1.1
 */
@Slf4j
@Service
@AllArgsConstructor
public class ResourceUploadServiceImpl implements ResourceUploadService {

    private final MinioClient minioClient;
    private final String bucket;
    private final RedisTemplate<String, String> redisTemplate;
    private final ResourceMapper resourceMapper;
    private final RabbitTemplate rabbitTemplate;

    // ---- 路径工具 ----

    private static String datePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    // ---- 文件类型分类 ----

    private static String fileType(String ext) {
        if (ext == null) return "other";
        return switch (ext.toLowerCase()) {
            case "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "md" -> "documents";
            case "jpg", "jpeg", "png", "gif", "bmp", "svg", "webp" -> "images";
            case "mp4", "avi", "mov", "mkv", "webm", "flv" -> "videos";
            case "mp3", "wav", "flac", "aac", "ogg" -> "audio";
            default -> "other";
        };
    }

    // ---- 路径工具 ----

    /** chunks/{type}/{date}/{md5_0}/{md5}/part_{n} */
    private static String chunkPath(String fileMd5, String ext, int chunkNo) {
        return String.format("chunks/%s/%s/%s/%s/part_%d",
                fileType(ext), datePath(), fileMd5.charAt(0), fileMd5, chunkNo);
    }

    /** chunks/{type}/{date}/{md5_0}/{md5}/ */
    private static String chunkPrefix(String fileMd5, String ext) {
        return String.format("chunks/%s/%s/%s/%s/", fileType(ext), datePath(), fileMd5.charAt(0), fileMd5);
    }

    /** resources/{userId}/{type}/{date}/{md5_0}/{md5}_{fileName} */
    private static String resourcePath(String userId, String fileMd5, String ext, String fileName) {
        return String.format("resources/%s/%s/%s/%s/%s_%s",
                userId, fileType(ext), datePath(), fileMd5.charAt(0), fileMd5, fileName);
    }

    // ---- 秒传 ----

    @Override
    public String checkByMd5(String fileMd5) {
        String id = redisTemplate.opsForValue().get(RedisConstant.RESOURCE_FILE_KEY + fileMd5);
        if (id != null) {
            return id;
        }
        Resource exist = resourceMapper.selectOne(
                new LambdaQueryWrapper<Resource>()
                        .eq(Resource::getFileMd5, fileMd5)
                        .eq(Resource::getParseStatus, 2));
        if (exist != null) {
            redisTemplate.opsForValue().set(RedisConstant.RESOURCE_FILE_KEY + fileMd5, exist.getId(), Duration.ofDays(7));
            return exist.getId();
        }
        return null;
    }

    // ---- 断点续传 ----

    @Override
    public Set<Integer> getUploadedChunks(String fileMd5) {
        Set<String> members = redisTemplate.opsForSet()
                .members(RedisConstant.RESOURCE_UPLOAD_KEY + fileMd5 + ":done");
        if (members == null || members.isEmpty()) {
            return Set.of();
        }
        return members.stream().map(Integer::parseInt).collect(Collectors.toSet());
    }

    // ---- 分片上传 ----

    @Override
    public void uploadChunk(String fileMd5, String ext, int chunkNo, int totalChunks, byte[] chunkData) {
        String key = RedisConstant.RESOURCE_UPLOAD_KEY + fileMd5;
        String object = chunkPath(fileMd5, ext, chunkNo);

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .stream(new ByteArrayInputStream(chunkData), chunkData.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );

            redisTemplate.opsForSet().add(key + ":done", String.valueOf(chunkNo));
            redisTemplate.opsForValue().set(key + ":total", String.valueOf(totalChunks),
                    Duration.ofHours(RedisConstant.RESOURCE_UPLOAD_EXPIRE_HOURS));
            redisTemplate.expire(key + ":done", Duration.ofHours(RedisConstant.RESOURCE_UPLOAD_EXPIRE_HOURS));

        } catch (Exception e) {
            log.error("上传分片失败: {} chunk {}", fileMd5, chunkNo, e);
            throw new ResourceUploadException("分片上传失败", e);
        }
    }

    // ---- 合并 ----

    @Override
    public UploadResult mergeChunks(String fileMd5, String fileName, String userId) {
        String existingId = checkByMd5(fileMd5);
        if (existingId != null) {
            return new UploadResult(existingId, fileName, true);
        }

        String key = RedisConstant.RESOURCE_UPLOAD_KEY + fileMd5;
        String total = redisTemplate.opsForValue().get(key + ":total");
        int totalChunks = total != null ? Integer.parseInt(total) : 0;
        String ext = getExtension(fileName);
        String prefix = chunkPrefix(fileMd5, ext);
        String objectKey = resourcePath(userId, fileMd5, ext, fileName);

        try {
            // compose 合并
            List<ComposeSource> sources = new ArrayList<>();
            for (int i = 0; i < totalChunks; i++) {
                sources.add(ComposeSource.builder()
                        .bucket(bucket)
                        .object(prefix + "part_" + i)
                        .build());
            }

            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .sources(sources)
                            .build()
            );

            // 清理分片（按前缀批量删）
            for (int i = 0; i < totalChunks; i++) {
                try {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(prefix + "part_" + i)
                                    .build());
                } catch (Exception ignored) {
                }
            }

        } catch (Exception e) {
            log.error("合并分片失败: {}", fileMd5, e);
            throw new ResourceUploadException("合并失败", e);
        }

        Resource resource = new Resource();
        resource.setId(UUID.randomUUID().toString().replace("-", ""));
        resource.setUserId(userId);
        resource.setTitle(fileName);
        resource.setFileName(fileName);
        resource.setFileExt(getExtension(fileName));
        resource.setFileMd5(fileMd5);
        resource.setStorageBucket(bucket);
        resource.setFilePath(objectKey);
        resource.setParseStatus(1);
        resource.setVectorStatus(0);
        resource.setCreatedAt(LocalDateTime.now());
        resourceMapper.insert(resource);

        redisTemplate.opsForValue().set(RedisConstant.RESOURCE_FILE_KEY + fileMd5, resource.getId(), Duration.ofDays(7));
        redisTemplate.delete(List.of(key + ":total", key + ":done"));

        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_DOC_PROCESS, resource.getId());

        log.info("文件合并完成: {} → {}", fileName, resource.getId());
        return new UploadResult(resource.getId(), fileName, false);
    }

    private String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(dot + 1).toLowerCase() : "";
    }
}
