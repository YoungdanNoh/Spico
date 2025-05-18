package com.a401.spicoandroid.infrastructure.datastore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDataStoreViewModel @Inject constructor(
    val userDataStore: UserDataStore
) : ViewModel()