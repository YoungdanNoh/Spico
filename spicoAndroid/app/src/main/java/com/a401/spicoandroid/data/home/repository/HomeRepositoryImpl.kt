package com.a401.spicoandroid.data.home.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.home.api.HomeApi
import com.a401.spicoandroid.domain.home.model.HomeReport
import com.a401.spicoandroid.domain.home.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {
    override suspend fun getRecentReports(): DataResource<List<HomeReport>> {
        return try {
            val response = api.getRecentReports()
            val reports = response.data?.reports?.map { it.toDomain() } ?: emptyList()
            DataResource.Success(reports)
        } catch (e: Exception) {
            DataResource.Error(e)
        }
    }
}
