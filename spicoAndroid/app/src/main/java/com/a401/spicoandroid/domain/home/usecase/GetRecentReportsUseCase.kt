package com.a401.spicoandroid.domain.home.usecase

import com.a401.spicoandroid.domain.home.repository.HomeRepository
import javax.inject.Inject

class GetRecentReportsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() = repository.getRecentReports()
}