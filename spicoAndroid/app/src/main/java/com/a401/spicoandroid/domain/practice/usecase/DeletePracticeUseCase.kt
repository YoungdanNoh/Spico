package com.a401.spicoandroid.domain.practice.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class DeletePracticeUseCase @Inject constructor(
    private val repository: PracticeRepository
) {
    suspend operator fun invoke(projectId: Int, practiceId: Int): DataResource<Unit> {
        return repository.deletePractice(projectId, practiceId)
    }
}