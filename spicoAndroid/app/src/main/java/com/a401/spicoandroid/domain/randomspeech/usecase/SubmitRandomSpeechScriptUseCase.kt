package com.a401.spicoandroid.domain.randomspeech.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.repository.RandomSpeechRepository
import javax.inject.Inject

class SubmitRandomSpeechScriptUseCase @Inject constructor(
    private val repository: RandomSpeechRepository
) {
    suspend operator fun invoke(speechId: Int, script: String): DataResource<Unit> {
        return repository.submitRandomSpeechScript(speechId, script)
    }
}
