package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore

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



