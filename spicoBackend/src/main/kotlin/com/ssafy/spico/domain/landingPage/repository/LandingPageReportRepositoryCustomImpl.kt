package com.ssafy.spico.domain.landingPage.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.PracticeStatus
import com.ssafy.spico.domain.practice.entity.PracticesEntity
import com.ssafy.spico.domain.practice.entity.QPracticesEntity.practicesEntity
import com.ssafy.spico.domain.project.entity.QProjectEntity.projectEntity
import org.springframework.stereotype.Repository

@Repository
class LandingPageReportRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : LandingPageReportRepositoryCustom {

    override fun findTop3ByUserIdOrderByCreatedAtDesc(userId: Int): List<PracticesEntity> {

        return queryFactory
            .select(practicesEntity)
            .from(practicesEntity)
            .join(practicesEntity.projectEntity, projectEntity).fetchJoin()
            .where(
                projectEntity.userEntity.id.eq(userId)
                    .and(practicesEntity.status.eq(PracticeStatus.COMPLETED)))
            .orderBy(practicesEntity.createdAt.desc())
            .limit(3)
            .fetch()
    }

}