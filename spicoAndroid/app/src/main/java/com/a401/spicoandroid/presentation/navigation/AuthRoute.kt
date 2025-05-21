package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthRoute(
    navController: NavHostController,
    userDataStore: UserDataStore,
    content: @Composable () -> Unit
) {
    val accessTokenState = userDataStore.observeAccessToken().collectAsState(initial = null)
    val accessToken = accessTokenState.value

    if (accessToken != null) {
        content()
    }
}