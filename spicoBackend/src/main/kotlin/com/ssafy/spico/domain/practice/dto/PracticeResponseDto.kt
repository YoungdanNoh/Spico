package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.model.Practice
import java.time.format.DateTimeFormatter

data class PracticeResponseDto(
    val practiceId: Int,
    val practiceType: String,
    val coachingCnt: Int?,
    val finalCnt: Int?,
    val createdAt: String
)

fun Practice.toResponse(): PracticeResponseDto {
    this.id?: throw Exception("Id is null")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return PracticeResponseDto(
        practiceId = this.id,
        practiceType = this.type.toKorean(),
        coachingCnt = this.projectEntity.lastCoachingCnt.takeIf { this.type == PracticeType.COACHING },
        finalCnt = this.projectEntity.lastFinalCnt.takeIf { this.type == PracticeType.FINAL },
        createdAt = this.createdAt.format(formatter)
     )
}