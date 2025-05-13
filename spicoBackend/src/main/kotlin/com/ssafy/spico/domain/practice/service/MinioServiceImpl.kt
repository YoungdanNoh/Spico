package com.ssafy.spico.domain.practice.service

import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MinioServiceImpl(
    @Value("\${endpoint}") private val endpoint: String,
    @Value("\${access-key}") private val accessKey: String,
    @Value("\${secret-key}") private val secretKey: String
) : MinioService {

    private val minioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

    override fun generatePresignedUrl(bucketName: String, objectName: String, expiryMinutes: Int): String {

        return minioClient.getPresignedObjectUrl(
            io.minio.GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .`object`(objectName)
                .expiry(expiryMinutes, TimeUnit.MINUTES)
                .build()
        )

    }

    override fun generatePresignedGetUrl(bucketName: String, objectName: String, expiryMinutes: Int): String {
        return minioClient.getPresignedObjectUrl(
            io.minio.GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)  // 🔹 PUT → GET
                .bucket(bucketName)
                .`object`(objectName)
                .expiry(expiryMinutes, TimeUnit.MINUTES)
                .build()
        )
    }
}