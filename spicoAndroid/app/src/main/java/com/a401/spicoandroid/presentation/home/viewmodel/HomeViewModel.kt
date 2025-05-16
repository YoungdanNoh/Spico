package com.a401.spicoandroid.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {

    val nickname: StateFlow<String?> = userDataStore.nickname.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
}