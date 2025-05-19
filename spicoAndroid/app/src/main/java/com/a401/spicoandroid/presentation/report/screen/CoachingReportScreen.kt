package com.a401.spicoandroid.presentation.report.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.ChipType
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.component.LoadingInProgressView
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.error.screen.NotFoundScreen
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.component.FeedbackCard
import com.a401.spicoandroid.presentation.report.component.ReportInfoHeader
import com.a401.spicoandroid.presentation.report.viewmodel.CoachingReportViewModel

@Composable
fun CoachingReportScreen(
    modifier: Modifier = Modifier,
    navController : NavController,
    projectId: Int,
    practiceId: Int,
    source: String = "script",
    viewModel: CoachingReportViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var isAlertVisible by remember { mutableStateOf(false) }

    val reportState by viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(projectId, practiceId) {
        viewModel.fetchCoachingReport(projectId, practiceId)
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로 가기",
                        onClick = {
                            when (source) {
                                "home" -> {
                                    navController.navigate(NavRoutes.Home.route) {
                                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                                    }
                                }
                                else -> {
                                    val targetRoute = NavRoutes.ProjectDetail.withId(projectId)
                                    navController.navigate(targetRoute) {
                                        popUpTo(NavRoutes.ProjectList.route) { inclusive = false }
                                    }
                                }
                            }
                        }
                    )
                },
                rightContent = {
                    CommonButton(
                        text = "삭제",
                        size = ButtonSize.XS,
                        backgroundColor = White,
                        textColor = Error,
                        borderColor = Error,
                        onClick = { isAlertVisible = true }
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        when {
            reportState.isLoading -> {
                LoadingInProgressView(
                    imageRes = R.drawable.character_home_5,
                    message = "리포트를 불러오고 있어요\n잠시만 기다려주세요!"
                )
            }

            reportState.error != null -> {
                NotFoundScreen(navController = navController)
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .background(BrokenWhite)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ReportInfoHeader(
                        imagePainter = painterResource(id = R.drawable.img_coaching_practice),
                        projectTitle = reportState.projectName,
                        roundCount = reportState.roundCount,
                        chipText = "코칭모드",
                        chipType = ChipType.REPORT_ACTION
                    )
                    FeedbackCard(
                        imageResId = R.drawable.img_feedback_volume,
                        title = "성량",
                        description = reportState.volumeStatus
                    )

                    FeedbackCard(
                        imageResId = R.drawable.img_feedback_speed,
                        title = "속도",
                        description = reportState.speedStatus
                    )

                    FeedbackCard(
                        imageResId = R.drawable.img_feedback_silence,
                        title = "휴지",
                        description = "휴지 기간이 총\n${reportState.pauseCount}회 있었어요"
                    )
                }
            }
        }

        if (isAlertVisible) {
            CommonAlert(
                title = "리포트를 삭제하시겠습니까?",
                confirmText = "삭제",
                onConfirm = {
                    isAlertVisible = false
                    viewModel.deleteReport(
                        projectId = projectId,
                        practiceId = practiceId,
                        onSuccess = {
                            Toast.makeText(context, "리포트를 삭제했어요", Toast.LENGTH_SHORT).show()
                            navController.navigate(NavRoutes.ProjectDetail.withId(projectId)) {
                                popUpTo(NavRoutes.ProjectList.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onError = {throwable ->
                            Toast.makeText(context, "삭제에 실패했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                            Log.e("CoachingReport", "❌ 삭제 실패: ${throwable.message}", throwable)
                        }
                    )
                },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                onCancel = {
                    isAlertVisible = false
                },
                onDismissRequest = {
                    isAlertVisible = false
                }
            )
        }
    }
}
