package com.a401.spicoandroid.domain.randomspeech.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo

interface RandomSpeechRepository {

    suspend fun createRandomSpeech(
        topic: String,
        prepTime: Int,
        speakTime: Int
    ): DataResource<RandomSpeechInitInfo>

    suspend fun submitRandomSpeechScript(
        speechId: Int,
        script: String
    ): DataResource<Unit>
}
