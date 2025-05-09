package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.VideoFeedbackPointsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VideoFeedbackPointsRepository : JpaRepository<VideoFeedbackPointsEntity, Int>, VideoFeedbackPointsRepositoryCustom {
}