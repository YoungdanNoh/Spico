package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.QFinalReportEntity.finalReportEntity
import org.springframework.stereotype.Repository

@Repository
class FinalReportRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FinalReportRepositoryCustom {

    override fun findLastCntByProject(projectId: Int): Int {
        return queryFactory.select(finalReportEntity.finalPracticeCnt)
            .from(finalReportEntity)
            .where(finalReportEntity.practiceEntity.projectEntity.projectId.eq(projectId))
            .orderBy(finalReportEntity.finalPracticeCnt.desc())
            .limit(1)
            .fetchOne() ?: 0
    }

}