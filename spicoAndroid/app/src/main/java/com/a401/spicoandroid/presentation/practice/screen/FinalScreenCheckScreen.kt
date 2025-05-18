package com.a401.spicoandroid.presentation.practice.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.component.CameraPreview
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel

@Composable
fun FinalScreenCheckScreen(
    navController: NavController,
    parentNavController: NavController,
    practiceViewModel: PracticeViewModel,
    finalModeViewModel: FinalModeViewModel
) {

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "화면 점검",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = { parentNavController.popBackStack()  }
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonButton(
                    text = "시작하기",
                    size = ButtonSize.LG,
                    onClick = {
                        practiceViewModel.createPractice(
                            onSuccess = {
                                val practiceId = practiceViewModel.practiceId.value
                                val projectId = practiceViewModel.selectedProject?.id

                                Log.d("FinalFlow", "✅ createPractice 성공: projectId=$projectId, practiceId=$practiceId")

                                if (practiceId != null && projectId != null) {

                                    if (practiceViewModel.hasAudience) {
                                        navController.navigate(NavRoutes.FinalModeAudience.withArgs(projectId, practiceId))
                                    } else {
                                        navController.navigate(NavRoutes.FinalModeVoice.withArgs(projectId, practiceId))
                                    }

                                    finalModeViewModel.setPracticeId(practiceId)
                                    finalModeViewModel.setHasQnA(practiceViewModel.hasQnA)
                                }
                            },
                            onFailure = {
                                Log.e("FinalFlow", "❌ createPractice 실패", it)
                            }
                        )

                    }

                )
            }
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // 상단 고정 텍스트
            Text(
                text = "모드가 시작되면 본인 모습을 볼 수 없습니다.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = Typography.titleLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.weight(1f))

            // 카메라 중앙 정렬
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}