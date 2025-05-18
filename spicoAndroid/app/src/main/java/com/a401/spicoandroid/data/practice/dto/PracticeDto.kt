package com.a401.spicoandroid.data.practice.dto

import com.a401.spicoandroid.domain.practice.model.Practice

data class PracticeDto(
    val practiceId: Int,
    val practiceName: String, // "코칭" or "파이널"
    val coachingCnt: Int?,     // 코칭일 때만 값 있음
    val finalCnt: Int?,        // 파이널일 때만 값 있음
    val createdAt: String      // "yyyy-MM-dd HH:mm"
)

fun PracticeDto.toDomain(): Practice {
    return Practice(
        id = practiceId,
        name = practiceName,
        count = finalCnt ?: coachingCnt ?: 0,
        createdAt = createdAt,
        finalCnt = finalCnt,
        coachingCnt = coachingCnt
    )
}
