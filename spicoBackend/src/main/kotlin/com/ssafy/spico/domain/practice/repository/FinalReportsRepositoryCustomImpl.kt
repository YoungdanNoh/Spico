package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.FinalReportsEntity
import com.ssafy.spico.domain.practice.entity.QFinalReportsEntity.finalReportsEntity
import org.springframework.stereotype.Repository

@Repository
class FinalReportsRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FinalReportsRepositoryCustom {

//    override fun findLastCntByProject(projectId: Int): Int {
//
//        return queryFactory.select(finalReportsEntity.finalPracticeCnt)
//            .from(finalReportsEntity)
//            .where(finalReportsEntity.practicesEntity.projectEntity.projectId.eq(projectId))
//            .orderBy(finalReportsEntity.finalPracticeCnt.desc())
//            .limit(1)
//            .fetchOne() ?: 0
//    }

    override fun findReportByPractice(practiceId: Int): FinalReportsEntity? {

        return queryFactory.select(finalReportsEntity)
            .from(finalReportsEntity)
            .where(finalReportsEntity.practicesEntity.practiceId.eq(practiceId))
            .fetchOne()
    }

}