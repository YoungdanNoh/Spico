package com.a401.spicoandroid.domain.finalmode.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.finalmode.model.FinalQuestion
import com.a401.spicoandroid.domain.finalmode.repository.FinalModeRepository
import javax.inject.Inject

class GenerateFinalQuestionsUseCase @Inject constructor(
    private val repository: FinalModeRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        practiceId: Int,
        speechContent: String
    ): DataResource<List<FinalQuestion>> {
        return repository.generateQuestions(projectId, practiceId, speechContent)
    }
}
