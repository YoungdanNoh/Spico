package com.a401.spicoandroid.domain.finalmode.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.finalmode.dto.FinalModeResultRequestDto
import com.a401.spicoandroid.domain.finalmode.model.FinalModeResult
import com.a401.spicoandroid.domain.finalmode.model.FinalQuestion

interface FinalModeRepository {
    suspend fun generateQuestions(
        projectId: Int,
        practiceId: Int,
        speechContent: String
    ): DataResource<List<FinalQuestion>>

    suspend fun finishFinalPractice(
        projectId: Int,
        practiceId: Int,
        request: FinalModeResultRequestDto
    ): DataResource<FinalModeResult>
}
