package com.a401.spicoandroid.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(
        MyPageState(
            name = "김도영",
            email = "homerunball@gmail.com"
        )
    )
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    fun onLogoutClicked() {
        // TODO: 로그아웃 처리
    }

    fun confirmWithdraw() {
        // TODO: 탈퇴 처리
    }
}
