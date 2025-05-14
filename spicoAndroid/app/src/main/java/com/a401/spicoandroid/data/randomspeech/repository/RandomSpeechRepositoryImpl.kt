package com.a401.spicoandroid.data.randomspeech.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.randomspeech.api.RandomSpeechApi
import com.a401.spicoandroid.data.randomspeech.dto.CreateRandomSpeechRequestDto
import com.a401.spicoandroid.data.randomspeech.dto.SubmitRandomSpeechScriptRequestDto
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo
import com.a401.spicoandroid.domain.randomspeech.repository.RandomSpeechRepository
import javax.inject.Inject

class RandomSpeechRepositoryImpl @Inject constructor(
    private val api: RandomSpeechApi
) : RandomSpeechRepository {

    override suspend fun createRandomSpeech(
        topic: String,
        prepTime: Int,
        speakTime: Int
    ): DataResource<RandomSpeechInitInfo> = safeApiCall {
        val request = CreateRandomSpeechRequestDto(
            topic = topic,
            preparationTime = prepTime,
            speechTime = speakTime
        )
        api.createRandomSpeech(request).getOrThrow { it.toDomain() }
    }

    override suspend fun submitRandomSpeechScript(
        speechId: Int,
        script: String
    ): DataResource<Unit> = safeApiCall {
        val request = SubmitRandomSpeechScriptRequestDto(script = script)
        api.submitRandomSpeechScript(speechId, request)
        Unit
    }
}
