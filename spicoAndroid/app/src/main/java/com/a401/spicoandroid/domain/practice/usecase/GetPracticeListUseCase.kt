package com.a401.spicoandroid.domain.practice.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.model.Practice
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class GetPracticeListUseCase @Inject constructor(
    private val repository: PracticeRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        filter: String,
        cursor: Int?,
        size: Int
    ): DataResource<List<Practice>> {
        return repository.getPracticeList(projectId, filter, cursor, size)
    }
}