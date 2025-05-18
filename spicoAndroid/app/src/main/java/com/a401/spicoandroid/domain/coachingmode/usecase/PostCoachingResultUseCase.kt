package com.a401.spicoandroid.domain.coachingmode.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.coachingmode.dto.CoachingResultRequestDto
import com.a401.spicoandroid.domain.coachingmode.repository.CoachingModeRepository
import javax.inject.Inject

class PostCoachingResultUseCase @Inject constructor(
    private val repository: CoachingModeRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        practiceId: Int,
        request: CoachingResultRequestDto
    ): DataResource<Unit> {
        return repository.postCoachingResult(projectId, practiceId, request)
    }
}