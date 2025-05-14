package com.a401.spicoandroid.data.practice.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.practice.api.PracticeApi
import com.a401.spicoandroid.data.practice.dto.toDomain
import com.a401.spicoandroid.domain.practice.model.Practice
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class PracticeRepositoryImpl @Inject constructor(
    private val api: PracticeApi
): PracticeRepository {
    override suspend fun getPracticeList(
        projectId: Int,
        filter: String,
        cursor: Int?,
        size: Int
    ): DataResource<List<Practice>> = safeApiCall {
        api.getPracticeList(projectId, filter, cursor, size)
            .getOrThrow { it.practices.map { dto -> dto.toDomain() } }
    }

}