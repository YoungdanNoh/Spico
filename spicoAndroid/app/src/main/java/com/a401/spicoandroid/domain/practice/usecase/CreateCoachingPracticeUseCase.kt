package com.a401.spicoandroid.domain.practice.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class CreateCoachingPracticeUseCase @Inject constructor(
    private val repository: PracticeRepository
) {
    suspend operator fun invoke(projectId: Int): DataResource<Int> {
        return repository.createCoachingPractice(projectId)
    }
}
