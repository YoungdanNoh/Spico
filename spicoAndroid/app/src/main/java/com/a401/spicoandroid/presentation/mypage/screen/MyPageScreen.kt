package com.a401.spicoandroid.presentation.mypage.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonBottomBar
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.Error
import com.a401.spicoandroid.common.ui.theme.Placeholder
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.presentation.mypage.component.WithdrawAlert

@Composable
fun MyPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
    onLogout: () -> Unit,
    onWithdraw: () -> Unit,
) {
    var showWithdrawDialog by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            CommonTopBar(
                centerText = "프로필",
            )
        },
        containerColor = White,
        bottomBar = {
            CommonBottomBar(
                navController = navController,
                onFabClick = onFabClick
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.character_coaching),
                contentDescription = "스피코 캐릭터 이미지",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            Text(
                text = "닉네임",
                style = Typography.headlineLarge,
            )
            Text(
                text = "김도영",
                style = Typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Placeholder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
            Text(
                text = "이메일",
                style = Typography.headlineLarge,
            )
            Text(
                text = "homerunball@gmail.com",
                style = Typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Placeholder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CommonButton(
                    text = "로그아웃",
                    size = ButtonSize.MD,
                    backgroundColor = TextTertiary,
                    borderColor = White,
                    textColor = White,
                    onClick = onLogout
                )
                CommonButton(
                    text = "계정탈퇴",
                    size = ButtonSize.MD,
                    backgroundColor = Error,
                    borderColor = White,
                    textColor = White,
                    onClick = { showWithdrawDialog = true }
                )
            }
        }
        if (showWithdrawDialog) {
            WithdrawAlert(
                onConfirm = {
                    showWithdrawDialog = false
                    onWithdraw()
                },
                onDismiss = { showWithdrawDialog = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 720,
    name = "MyPageScreen Preview"
)
@Composable
fun PreviewMyPageScreen() {
    val navController = rememberNavController()

    SpeakoAndroidTheme {
        MyPageScreen(
            navController = navController,
            onFabClick = {},
            onWithdraw = {},
            onLogout = {}
        )
    }
}