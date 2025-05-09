package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.bottomsheet.DeleteModalBottomSheet
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.randomspeech.dummy.DummyRandomSpeechList
import com.a401.spicoandroid.presentation.randomspeech.util.getTopicIconRes

@Composable
fun RandomSpeechProjectListScreen(
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    val projectList = DummyRandomSpeechList
//    val projectList = emptyList<RandomSpeech>()

    val navController = LocalNavController.current

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    //  바텀시트
    if (showBottomSheet) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x33222222)) // 222222 + 20% 투명
        ) {
            DeleteModalBottomSheet(
                onDeleteClick = {
                    showBottomSheet = false
                    showAlert = true
                },
                onDismissRequest = { showBottomSheet = false }
            )
        }
    }

    // 알림창
    if (showAlert) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x33222222))
        ) {
            CommonAlert(
                title = "리포트가 삭제됩니다.\n정말 삭제하시겠습니까?",
                confirmText = "삭제",
                onConfirm = {
                    // TODO: 삭제 로직
                    showAlert = false
                },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                onCancel = { showAlert = false },
                cancelTextColor = TextTertiary,
                cancelBackgroundColor = BackgroundSecondary,
                cancelBorderColor = BackgroundSecondary,
                borderColor = White,
                onDismissRequest = { showAlert = false }
            )
        }
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트 목록",
                leftContent = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left_black), // ← ← 아이콘 리소스 필요
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                rightContent = {
                    CommonButton(
                        text = "시작",
                        size = ButtonSize.XS,
                        backgroundColor = Action,
                        borderColor = Action,
                        textColor = White,
                        onClick = onStartClick,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        if (projectList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.character_home_1),
                        contentDescription = "랜덤스피치 없음 이미지",
                        modifier = Modifier.size(140.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "리포트가 없어요.\n랜덤스피치를 시작해보세요!",
                        style = Typography.titleLarge.copy(lineHeight = 28.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(projectList) { item ->
                    CommonList(
                        imagePainter = painterResource(id = getTopicIconRes(item.topic)),
                        title = item.title,
                        description = formatDateWithDay(item.dateTime),
                        onClick = {
                            onProjectClick(item.id)  // 상세 이동
                        },
                        onLongClick = {
                            selectedId = item.id
                            showBottomSheet = true  // 바텀 시트
                        }
                    )
                }
            }
        }
    }
}
