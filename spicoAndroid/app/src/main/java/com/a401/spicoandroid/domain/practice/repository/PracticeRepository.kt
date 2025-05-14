package com.a401.spicoandroid.domain.practice.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.domain.practice.model.Practice

interface PracticeRepository {
    suspend fun getPracticeList(
        projectId: Int,
        filter: String,
        cursor: Int?,
        size: Int
    ): DataResource<List<Practice>>

    suspend fun createCoachingPractice(
        projectId: Int): DataResource<Int>

    suspend fun createFinalPractice(
        projectId: Int, request: FinalPracticeRequest
    ): DataResource<Int>

    suspend fun deletePractice(
        projectId: Int,
        practiceId: Int
    ): DataResource<Unit>
}
