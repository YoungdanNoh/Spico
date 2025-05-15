package com.a401.spicoandroid.infrastructure.speech

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun SpeechTestScreen(navController: NavController) {
    SpeakoAndroidTheme {
        Scaffold(
            containerColor = BackgroundPrimary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpeechStartButton(
                    onClick = {
                        Log.d("SpeechTestScreen", "STT 시작 버튼 클릭됨")
                        // 여기에 STT 시작 로직 연결
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReturnHomeButton {
                    navController.navigate(NavRoutes.Home.route)
                }
            }
        }
    }
}

@Composable
fun SpeechStartButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Action else Disabled,
            contentColor = White,
            disabledContainerColor = Disabled,
            disabledContentColor = TextTertiary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "시작하기",
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun ReturnHomeButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "홈으로 돌아가기",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )
    }
}
