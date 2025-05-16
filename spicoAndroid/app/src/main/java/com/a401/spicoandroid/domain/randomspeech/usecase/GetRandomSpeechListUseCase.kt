package com.a401.spicoandroid.domain.randomspeech.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechSummary
import com.a401.spicoandroid.domain.randomspeech.repository.RandomSpeechRepository
import javax.inject.Inject

class GetRandomSpeechListUseCase @Inject constructor(
    private val repository: RandomSpeechRepository
) {
    suspend operator fun invoke(): DataResource<List<RandomSpeechSummary>> {
        return repository.getRandomSpeechList()
    }
}
