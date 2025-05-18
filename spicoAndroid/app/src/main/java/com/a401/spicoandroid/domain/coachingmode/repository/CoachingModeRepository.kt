package com.a401.spicoandroid.domain.coachingmode.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.coachingmode.dto.CoachingResultRequestDto

interface CoachingModeRepository {
    suspend fun postCoachingResult(
        projectId: Int,
        practiceId: Int,
        request: CoachingResultRequestDto
    ): DataResource<Unit>
}