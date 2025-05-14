package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.usecase.GetPracticeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val getPracticeListUseCase: GetPracticeListUseCase
) : ViewModel() {

    private val _practiceListState = MutableStateFlow(PracticeListState())
    val practiceListState: StateFlow<PracticeListState> = _practiceListState.asStateFlow()

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
}
