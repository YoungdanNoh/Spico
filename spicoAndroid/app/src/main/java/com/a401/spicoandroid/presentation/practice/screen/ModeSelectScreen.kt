package com.a401.spicoandroid.presentation.practice.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.theme.Pretendard
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.presentation.practice.component.ModeSelectOptionCard
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeMode
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel

@Composable
fun ModeSelectScreen(
    navController: NavController,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = {
                            viewModel.reset()
                            navController.popBackStack()
                        }
                    )
                }
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "안녕하세요,\n어떻게 발표 연습을\n하고 싶나요?",
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    letterSpacing = 0.96.sp
                ),
                color = TextPrimary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ModeSelectOptionCard(
                    title = "코칭모드",
                    description = "발표 음성을 분석하여\n실시간 AI 피드백을 받을 수 있습니다.",
                    chips = listOf("# 대본있음", "# 음성녹음"),
                    imageRes = R.drawable.img_coaching_practice,
                    onClick = {
                        viewModel.selectedMode = PracticeMode.COACHING // ✅ 저장
                        navController.navigate("project_select")
                    }
                )

                ModeSelectOptionCard(
                    title = "파이널모드",
                    description = "실제 발표처럼 청중 앞에서 발표합니다.\nAI 질의 응답도 진행합니다.",
                    chips = listOf("# 대본없음", "# 영상녹화"),
                    imageRes = R.drawable.img_final_practice,
                    onClick = {
                        viewModel.selectedMode = PracticeMode.FINAL // ✅ 저장
                        navController.navigate("project_select")
                    }
                )
            }
        }
    }
}

