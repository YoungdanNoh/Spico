package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.CoachingReportsEntity
import com.ssafy.spico.domain.practice.entity.QCoachingReportsEntity.coachingReportsEntity
import org.springframework.stereotype.Repository

@Repository
class CoachingReportsRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : CoachingReportsRepositoryCustom {

    override fun findReportByPractice(practiceId: Int): CoachingReportsEntity? {

        return queryFactory.select(coachingReportsEntity)
            .from(coachingReportsEntity)
            .where(coachingReportsEntity.practicesEntity.practiceId.eq(practiceId))
            .fetchOne()
    }

}