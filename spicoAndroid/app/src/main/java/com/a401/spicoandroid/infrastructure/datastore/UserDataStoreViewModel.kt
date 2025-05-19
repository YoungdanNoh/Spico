package com.a401.spicoandroid.infrastructure.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class UserDataStoreViewModel @Inject constructor(
    val userDataStore: UserDataStore
) : ViewModel() {

    // accessToken Flow 그대로 노출
    val accessToken: Flow<String?> = userDataStore.accessToken

    // 앱 시작 시 토큰 만료되었는지 확인하고, 만료되었으면 삭제
    init {
        viewModelScope.launch {
            userDataStore.autoClearIfExpired()
        }
    }

    // 수동 로그아웃 처리
    suspend fun logout() {
        userDataStore.clear()
    }
}
