package com.a401.spicoandroid.data.practice.dto

import com.a401.spicoandroid.domain.practice.model.FinalSetting

data class FinalSettingResponseDto(
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)

fun FinalSettingResponseDto.toModel(): FinalSetting {
    return FinalSetting(
        hasAudience = hasAudience,
        hasQnA = hasQnA,
        questionCount = questionCount,
        answerTimeLimit = answerTimeLimit
    )
}
