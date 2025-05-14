package com.a401.spicoandroid.domain.practice.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class CreateFinalPracticeUseCase @Inject constructor(
    private val repository: PracticeRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        request: FinalPracticeRequest
    ): DataResource<Int> {
        return repository.createFinalPractice(projectId, request)
    }
}
