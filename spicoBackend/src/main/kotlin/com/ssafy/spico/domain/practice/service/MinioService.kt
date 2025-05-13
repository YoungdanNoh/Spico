package com.ssafy.spico.domain.practice.service

interface MinioService {

    // 동영상 저장 시 사용
    fun generatePresignedUrl(bucketName: String, objectName: String, expiryMinutes: Int = 10): String

    // 동영상 재생 시 사용
    fun generatePresignedGetUrl(bucketName: String, objectName: String, expiryMinutes: Int = 360): String
    
}