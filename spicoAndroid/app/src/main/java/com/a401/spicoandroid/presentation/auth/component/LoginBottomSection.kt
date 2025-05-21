package com.a401.spicoandroid.presentation.auth.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonCircularProgressBar

@Composable
fun LoginBottomSection(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalCount: Int,
    onSelect: (Int) -> Unit,
    onKakaoLoginClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonCircularProgressBar(
            selectedIndex = currentPage,
            onSelect = onSelect,
            totalCount = totalCount
        )

        Spacer(modifier = Modifier.height(24.dp))

        KakaoLoginButton(onClick = onKakaoLoginClick)

        Spacer(modifier = Modifier.height(32.dp))
    }
}
