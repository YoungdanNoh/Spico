package com.a401.spicoandroid.presentation.auth.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "로그인")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LoginScreen 스켈레톤")
                Spacer(modifier = Modifier.height(16.dp))
                CommonButton(
                    text = "로그인",
                    onClick = onLoginSuccess
                )
            }
        }
    }
}