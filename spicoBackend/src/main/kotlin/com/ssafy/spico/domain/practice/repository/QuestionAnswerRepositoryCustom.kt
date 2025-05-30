package com.ssafy.spico.domain.practice.repository

import com.ssafy.spico.domain.practice.entity.QuestionAnswerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionAnswerRepositoryCustom {

    fun findQaByFinal(finalReportId: Int): List<QuestionAnswerEntity>?
}