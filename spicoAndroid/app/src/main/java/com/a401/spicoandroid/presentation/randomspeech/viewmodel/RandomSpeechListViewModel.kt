package com.a401.spicoandroid.presentation.randomspeech.viewmodel

import com.a401.spicoandroid.common.domain.DataResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechSummary
import com.a401.spicoandroid.domain.randomspeech.usecase.GetRandomSpeechListUseCase
import com.a401.spicoandroid.domain.report.usecase.DeleteRandomSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomSpeechListViewModel @Inject constructor(
    private val getListUseCase: GetRandomSpeechListUseCase,
    private val deleteUseCase: DeleteRandomSpeechUseCase
) : ViewModel() {

    var speechList by mutableStateOf<List<RandomSpeechSummary>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchList() {
        val launch = viewModelScope.launch {
            isLoading = true
            when (val result = getListUseCase()) {
                is DataResource.Success -> {
                    speechList = result.data
                    isLoading = false
                }

                is DataResource.Error -> {
                    errorMessage = result.throwable.message
                    isLoading = false
                }

                else -> {}
            }
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            when (val result = deleteUseCase(id)) {
                is DataResource.Success -> {
                    fetchList() // 삭제 후 리스트 갱신
                }
                is DataResource.Error -> {
                    errorMessage = result.throwable.message
                }
                else -> {}
            }
        }
    }
}
