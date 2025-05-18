package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.infrastructure.datastore.UserDataStoreViewModel
@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val userDataStore = hiltViewModel<UserDataStoreViewModel>().userDataStore
    val accessToken by userDataStore.observeAccessToken().collectAsState(initial = null)

    if (accessToken == null) {
        LoginNavGraph(navController = navController, userDataStore = userDataStore, modifier = modifier)
    } else {
        MainNavGraph(navController = navController, userDataStore = userDataStore, modifier = modifier)
    }
}

