package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.QVideoFeedbackPointsEntity.videoFeedbackPointsEntity
import com.ssafy.spico.domain.practice.entity.VideoFeedbackPointsEntity
import org.springframework.stereotype.Repository

@Repository
class VideoFeedbackPointsRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
): VideoFeedbackPointsRepositoryCustom {

    override fun findFeedbackByReport(finalReportId: Int): List<VideoFeedbackPointsEntity>? {
        return queryFactory.selectFrom(videoFeedbackPointsEntity)
            .where(
                videoFeedbackPointsEntity.finalReportsEntity.finalReportId.eq(finalReportId)
            )
            .fetch()
    }

}