package com.cuizhi.resource.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author thpaperman
 * @Description MinIO 对象存储客户端配置（自动建桶）
 * @Date 2026/7/7
 * @Version 1.1
 */
@Slf4j
@Configuration
public class OssConfig {

    @Getter
    @Value("${minio.bucket:cuizhi-resource}")
    private String bucket;

    @Value("${minio.endpoint:http://localhost:9000}")
    private String endpoint;

    @Value("${minio.access-key:minioadmin}")
    private String accessKey;

    @Value("${minio.secret-key:minioadmin}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public String minioBucket() {
        return bucket;
    }

    @PostConstruct
    public void ensureBucket() {
        try {
            MinioClient client = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                client.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build());
                log.info("MinIO 自动创建 bucket: {}", bucket);
            }
        } catch (Exception e) {
            log.error("MinIO bucket 初始化失败: {}", bucket, e);
        }
    }
}
