package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.usecase.GetPracticeListUseCase
import com.a401.spicoandroid.domain.practice.usecase.DeletePracticeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val getPracticeListUseCase: GetPracticeListUseCase,
    private val deletePracticeUseCase: DeletePracticeUseCase
) : ViewModel() {

    private val _practiceListState = MutableStateFlow(PracticeListState())
    val practiceListState: StateFlow<PracticeListState> = _practiceListState.asStateFlow()

    private val _practiceDeleteState = MutableStateFlow(PracticeDeleteState())
    val practiceDeleteState: StateFlow<PracticeDeleteState> = _practiceDeleteState.asStateFlow()

    // 연습 목록 조회
    fun fetchPracticeList(projectId: Int, filter: String, cursor: Int?, size: Int) {
        viewModelScope.launch {
            _practiceListState.update { it.copy(isLoading = true, error = null) }

            when (val result = getPracticeListUseCase(projectId, filter, cursor, size)) {
                is DataResource.Success -> {
                    _practiceListState.update {
                        it.copy(practices = result.data, isLoading = false)
                    }
                }
                is DataResource.Error -> {
                    _practiceListState.update {
                        it.copy(error = result.throwable, isLoading = false)
                    }
                }
                is DataResource.Loading -> {
                    _practiceListState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    // 연습 삭제
    fun deletePractice(projectId: Int, practiceId: Int) {
        viewModelScope.launch {
            _practiceDeleteState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            when (val result = deletePracticeUseCase(projectId, practiceId)) {
                is DataResource.Success -> {
                    _practiceDeleteState.update { it.copy(isSuccess = true, isLoading = false) }
                }
                is DataResource.Error -> {
                    _practiceDeleteState.update { it.copy(isLoading = false, error = result.throwable) }
                }
                is DataResource.Loading -> {
                    _practiceDeleteState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
    fun resetDeleteState() {
        _practiceDeleteState.value = PracticeDeleteState()
    }


}
