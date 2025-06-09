package ru.ssugt.drones.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${spring.minio.endpoint}")
    private String minioUrl;

    @Value("${spring.minio.bucket:drones}")
    private String bucketName;
    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials("minioadmin", "minioadmin")
                .build();

        createBucketIfNotExists(client);
        return client;
    }

    private void createBucketIfNotExists(MinioClient client) throws Exception {
        boolean isExist = client.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());

        if (!isExist) {
            client.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }
}
