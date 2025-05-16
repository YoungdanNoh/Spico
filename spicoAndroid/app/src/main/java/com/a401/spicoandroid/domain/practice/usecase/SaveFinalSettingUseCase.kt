package com.a401.spicoandroid.domain.practice.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.domain.practice.model.FinalSetting
import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import javax.inject.Inject

class SaveFinalSettingUseCase @Inject constructor(
    private val repository: PracticeRepository
) {
    suspend operator fun invoke(request: FinalPracticeRequest): DataResource<FinalSetting> {
        return repository.saveFinalSetting(request)
    }
}
