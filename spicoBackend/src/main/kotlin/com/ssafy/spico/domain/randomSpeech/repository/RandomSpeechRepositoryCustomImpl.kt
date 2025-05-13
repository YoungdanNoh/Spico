package com.ssafy.spico.domain.randomSpeech.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.spico.domain.randomSpeech.entity.QRandomSpeechEntity
import com.ssafy.spico.domain.randomSpeech.entity.RandomSpeechEntity
import org.springframework.stereotype.Repository

@Repository
class RandomSpeechRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
): RandomSpeechRepositoryCustom {

    override fun findRandomSpeechesByUserId(userId: Int): List<RandomSpeechEntity> {
        val randomSpeech = QRandomSpeechEntity.randomSpeechEntity

        return queryFactory.selectFrom(randomSpeech)
            .where(randomSpeech.userEntity.id.eq(userId))
            .orderBy(randomSpeech.createdAt.desc())
            .fetch()

    }
}