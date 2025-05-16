package com.a401.spicoandroid.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeNickname().collect { nickname  ->
                _state.value = MyPageState(name = nickname?: "")
            }
        }
    }

    fun onLogoutClicked() {
        // TODO: 로그아웃 처리
    }

    fun confirmWithdraw() {
        // TODO: 탈퇴 처리
    }
}
