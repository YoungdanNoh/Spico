package com.a401.spicoandroid.presentation.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.Pretendard

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(184.dp)
            .height(45.dp)
            .background(color = Color(0xFFFEE500), shape = RoundedCornerShape(6.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_kakao_logo),
                contentDescription = "카카오 로고",
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = "카카오 로그인",
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                color = Color(0xFF191600),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun KakaoLoginButtonPreview() {
    com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KakaoLoginButton(onClick = {})
        }
    }
}