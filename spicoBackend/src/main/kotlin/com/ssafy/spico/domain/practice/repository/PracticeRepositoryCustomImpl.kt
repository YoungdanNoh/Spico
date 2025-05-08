package com.ssafy.spico.domain.practice.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.practice.entity.PracticeEntity
import org.springframework.stereotype.Repository
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.entity.QPracticeEntity.practiceEntity

@Repository
class PracticeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PracticeRepositoryCustom {

    override fun findFinalPracticeByProjectId(projectId: Int): List<PracticeEntity> {
        return queryFactory.selectFrom(practiceEntity)
            .where(
                practiceEntity.projectEntity.projectId.eq(projectId),
                practiceEntity.type.eq(PracticeType.FINAL)
            )
            .fetch()
    }
}