package com.a401.spicoandroid.data.coachingmode.repository

import android.util.Log
import com.a401.spicoandroid.common.data.dto.getOrThrowNull
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.coachingmode.api.CoachingModeApi
import com.a401.spicoandroid.data.coachingmode.dto.CoachingResultRequestDto
import com.a401.spicoandroid.domain.coachingmode.repository.CoachingModeRepository
import javax.inject.Inject

class CoachingModeRepositoryImpl @Inject constructor(
    private val api: CoachingModeApi
) : CoachingModeRepository{
    override suspend fun postCoachingResult(
        projectId: Int,
        practiceId: Int,
        request: CoachingResultRequestDto
    ): DataResource<Unit> = safeApiCall {
        Log.d(
            "API_CALL", """
        ▶️ POST /projects/$projectId/practices/coaching/$practiceId/result
        📦 Body: $request
        """.trimIndent()
        )
        api.postCoachingResult(projectId, practiceId, request).getOrThrowNull { Unit }
    }
}