package com.a401.spicoandroid.presentation.error.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.EmptyStateView
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun NotFoundScreen(
    navController: NavController
) {
    EmptyStateView(
        imageRes = R.drawable.character_home_2,
        message = "요청하신 페이지를 찾을 수 없어요.",
        subActionText = "홈으로 가기",
        onSubActionClick = { navController.navigate(NavRoutes.Home.route) }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun NotFoundScreenPreview() {
    val fakeNavController = rememberNavController()

    SpeakoAndroidTheme {
        NotFoundScreen(navController = fakeNavController)
    }
}
