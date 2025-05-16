package com.a401.spicoandroid.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.home.model.HomeReport
import com.a401.spicoandroid.domain.home.usecase.GetRecentReportsUseCase
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val getRecentReportsUseCase: GetRecentReportsUseCase
) : ViewModel() {

    val nickname: StateFlow<String?> = userDataStore.nickname.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private val _recentReports = MutableStateFlow<List<HomeReport>>(emptyList())
    val recentReports: StateFlow<List<HomeReport>> = _recentReports

    init {
        loadRecentReports()
    }

    private fun loadRecentReports() {
        viewModelScope.launch {
            when (val result: DataResource<List<HomeReport>> = getRecentReportsUseCase()) {
                is DataResource.Success -> _recentReports.value = result.data
                is DataResource.Error -> { /* TODO: 에러 처리 */ }
                else -> Unit
            }
        }
    }
}