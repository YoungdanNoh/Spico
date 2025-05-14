package com.a401.spicoandroid.domain.randomspeech.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo
import com.a401.spicoandroid.domain.randomspeech.repository.RandomSpeechRepository
import javax.inject.Inject

class CreateRandomSpeechUseCase @Inject constructor(
    private val repository: RandomSpeechRepository
) {
    suspend operator fun invoke(
        topic: String,
        prepTime: Int,
        speakTime: Int
    ): DataResource<RandomSpeechInitInfo> {
        return repository.createRandomSpeech(topic, prepTime, speakTime)
    }
}
