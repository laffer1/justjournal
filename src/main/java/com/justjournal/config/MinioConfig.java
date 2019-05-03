package com.justjournal.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lucas Holt
 */
@Configuration
public class MinioConfig {
    @Value("${app.minio.host}")
    private String minioHost;

    @Value("${app.minio.accessKey}")
    private String minioAccessKey;

    @Value("${app.minio.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(minioHost, minioAccessKey, minioSecretKey);
    }
}
