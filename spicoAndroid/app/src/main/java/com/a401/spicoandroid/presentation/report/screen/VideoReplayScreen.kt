package com.a401.spicoandroid.presentation.report.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.activity.compose.BackHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.viewmodel.VideoReplayViewModel
import com.a401.spicoandroid.ui.component.CommonLinearProgressBar
import com.a401.spicoandroid.ui.component.TimeSegment
import kotlinx.coroutines.delay

@Composable
fun VideoReplayScreen(
    navController: NavController,
    videoUrl: String,
    viewModel: VideoReplayViewModel = hiltViewModel()
) {
    val player = viewModel.player
    var isPlaying by remember { mutableStateOf(false) }

    // 갱신: ExoPlayer 상태 감지해서 sync 하고 싶다면 추후 `player.isPlaying` StateFlow로도 가능

    val feedbackText = "첫 부분에서 너무 빨랐어요.\n다시 확인해보세요."

    val totalStartMillis = 60_000L
    val totalEndMillis = 90_000L
    val duration = totalEndMillis - totalStartMillis

    val segments = listOf(TimeSegment(totalStartMillis, totalEndMillis))

    // 현재 재생 위치 추적
    val currentPosition by produceState(initialValue = 0L) {
        while (true) {
            value = player.currentPosition
            delay(300L)
        }
    }

    val progress = ((currentPosition - totalStartMillis).toFloat() / duration).coerceIn(0f, 1f)

    val onSeek: (Float) -> Unit = { ratio ->
        val seekTo = totalStartMillis + (ratio * duration).toLong()
        player.seekTo(seekTo)
    }

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(videoUrl) {
        viewModel.setMedia(Uri.parse(videoUrl))
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "발표 영상 다시 보기",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.arrow_left,
                        contentDescription = "뒤로 가기",
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CommonButton(
                    text = "리포트 보기",
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "자율 프로젝트 최종발표",
                style = Typography.displayMedium,
                color = TextPrimary
            )

            VideoPlayerView(
                player = player,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.95f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                IconCircleButton(
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (isPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_white
                            ),
                            contentDescription = "재생 버튼",
                            tint = White
                        )
                    },
                    backgroundColor = Action,
                    enabled = true,
                    onClick = {
                        if (player.isPlaying) {
                            player.pause()
                            isPlaying = false
                        } else {
                            player.play()
                            isPlaying = true
                        }
                    }
                )

                Column {
                    Text(
                        text = "1:00 ~ 1:30",
                        style = Typography.bodySmall,
                        color = TextTertiary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CommonLinearProgressBar(
                        totalStartMillis = totalStartMillis,
                        totalEndMillis = totalEndMillis,
                        segments = segments,
                        progress = progress,
                        onSeek = onSeek,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            CommonFeedback(FeedbackType.Replay(feedbackText))
        }
    }
}
