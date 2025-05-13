package com.a401.spicoandroid.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.infrastructure.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.userInfoFlow.collect { userInfo ->
                _state.value = MyPageState(
                    name = userInfo.name,
                    email = userInfo.email
                )
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
