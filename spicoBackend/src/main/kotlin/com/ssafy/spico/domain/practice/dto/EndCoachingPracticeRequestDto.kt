package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import com.ssafy.spico.domain.practice.model.EndCoachingPractice

data class EndCoachingPracticeRequestDto(
    val fileName: String?,
    val pronunciationScore: Int?,
    val pauseCount: Int?,
    val volumeStatus: VolumeType?,
    val speedStatus: SpeedType?

)

fun EndCoachingPracticeRequestDto.toModel(): EndCoachingPractice {
    return EndCoachingPractice(
        fileName = this.fileName,
        pronunciationScore = this.pronunciationScore,
        pauseCount = this.pauseCount,
        volumeStatus = this.volumeStatus,
        speedStatus = this.speedStatus
    )
}
