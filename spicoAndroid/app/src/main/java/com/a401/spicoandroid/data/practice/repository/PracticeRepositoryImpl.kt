package com.a401.spicoandroid.data.practice.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.practice.api.PracticeApi
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.data.practice.dto.toDomain
import com.a401.spicoandroid.data.practice.dto.toModel
import com.a401.spicoandroid.domain.practice.model.FinalSetting
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

    override suspend fun createCoachingPractice(projectId: Int): DataResource<Int> {
        return try {
            val response = api.createCoachingPractice(projectId)
            if (response.success && response.data != null) {
                DataResource.Success(response.data.practiceId)
            } else {
                DataResource.Error(Throwable(response.errorMsg ?: "알 수 없는 오류"))
            }
        } catch (e: Exception) {
            DataResource.Error(e)
        }
    }
    override suspend fun getFinalSetting(): DataResource<FinalSetting> {
        return try {
            val response = api.getFinalSetting()
            if (response.success && response.data != null) {
                DataResource.Success(response.data.toModel())
            } else {
                DataResource.Error(Exception(response.errorMsg ?: "Unknown error"))
            }
        } catch (e: Exception) {
            DataResource.Error(e)
        }
    }

    override suspend fun saveFinalSetting(request: FinalPracticeRequest): DataResource<FinalSetting> {
        return try {
            val response = api.saveFinalSetting(request)
            if (response.success && response.data != null) {
                DataResource.Success(response.data.toModel())
            } else {
                DataResource.Error(Exception(response.errorMsg ?: "Unknown error"))
            }
        } catch (e: Exception) {
            DataResource.Error(e)
        }
    }


    override suspend fun createFinalPractice(
        projectId: Int,
        request: FinalPracticeRequest
    ): DataResource<Int> = safeApiCall {
        api.createFinalPractice(projectId, request)
            .getOrThrow { it.practiceId }
    }

    override suspend fun deletePractice(
        projectId: Int,
        practiceId: Int
    ): DataResource<Unit> = safeApiCall {
        api.deletePractice(projectId, practiceId).getOrThrow { Unit }
    }

}