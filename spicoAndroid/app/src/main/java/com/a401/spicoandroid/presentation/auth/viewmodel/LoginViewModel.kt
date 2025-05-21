package com.a401.spicoandroid.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.auth.AuthRepository
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataStore: UserDataStore // accessToken 확인
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    // 로그인 성공 여부
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    fun onLoginClicked(accessToken: String) {
        viewModelScope.launch {
            try {
                authRepository.login(accessToken).collect { result ->
                    when(result) {
                        is DataResource.Loading -> {
                            println("로그인 중")
                        }
                        is DataResource.Success -> {
                            val data = result.data
                            println("로그인 성공 - token: ${data.accessToken}, nickname: ${data.nickname}")
                            _loginSuccess.value = true
                        }
                        is DataResource.Error -> {
                            println("로그인 실패")
                        }
                    }
                }
            } catch (e: Exception){
                println("authRepository.login 내부 처리 에러")
            }
        }
    }

    fun resetLoginState() {
        _loginSuccess.value = false
    }
}
