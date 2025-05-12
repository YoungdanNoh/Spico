package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.QQuestionAnswerEntity.questionAnswerEntity
import com.ssafy.spico.domain.practice.entity.QuestionAnswerEntity
import org.springframework.stereotype.Repository

@Repository
class QuestionAnswerRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
): QuestionAnswerRepositoryCustom {

    override fun findQaByFinal(finalReportId: Int): List<QuestionAnswerEntity>?{
        return queryFactory.selectFrom(questionAnswerEntity)
            .where(
                questionAnswerEntity.finalReportsEntity.finalReportId.eq(finalReportId)
            )
            .fetch()
    }
}