package com.a401.spicoandroid.domain.finalmode.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.finalmode.dto.FinalModeResultRequestDto
import com.a401.spicoandroid.domain.finalmode.model.FinalModeResult
import com.a401.spicoandroid.domain.finalmode.repository.FinalModeRepository
import javax.inject.Inject

class FinishFinalPracticeUseCase @Inject constructor(
    private val repository: FinalModeRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        practiceId: Int,
        request: FinalModeResultRequestDto
    ): DataResource<FinalModeResult> {
        return repository.finishFinalPractice(projectId, practiceId, request)
    }
}