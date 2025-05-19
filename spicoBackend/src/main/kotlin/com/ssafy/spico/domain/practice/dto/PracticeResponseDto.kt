package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.model.Practice
import com.ssafy.spico.domain.practice.repository.CoachingReportsRepository
import com.ssafy.spico.domain.practice.repository.FinalReportsRepository
import java.time.format.DateTimeFormatter

data class PracticeResponseDto(
    val practiceId: Int,
    val practiceType: String,
    val coachingCnt: Int?,
    val finalCnt: Int?,
    val createdAt: String
)

fun Practice.toResponse(
    coachingReportsRepository: CoachingReportsRepository,
    finalReportsRepository: FinalReportsRepository
): PracticeResponseDto {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val coachingPracticeCnt = if (this.type == PracticeType.COACHING) {
        coachingReportsRepository.findReportByPractice(this.id!!)?.coachingPracticeCnt
    } else null

    val finalPracticeCnt = if (this.type == PracticeType.FINAL) {
        finalReportsRepository.findReportByPractice(this.id!!)?.finalPracticeCnt
    } else null

    return PracticeResponseDto(
        practiceId = this.id!!,
        practiceType = this.type.toKorean(),
        coachingCnt = coachingPracticeCnt,
        finalCnt = finalPracticeCnt,
        createdAt = this.createdAt.format(formatter)
    )
}