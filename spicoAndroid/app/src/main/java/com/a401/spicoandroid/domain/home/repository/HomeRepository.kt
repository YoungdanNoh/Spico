package com.a401.spicoandroid.domain.home.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.home.model.HomeReport

interface HomeRepository {
    suspend fun getRecentReports(): DataResource<List<HomeReport>>
}
