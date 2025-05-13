package com.a401.spicoandroid.presentation.project.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatTimeOnly
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectInfoDialog(
    navController: NavHostController,
    dateTitle: String, // ex: "4월 25일"
    projectList: List<ProjectSchedule>,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OverlayDark20)
                .clickable(onClick = onDismiss)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 헤더
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = dateTitle, style = Typography.headlineLarge, color = TextPrimary)
                        Text(
                            text = "총 ${projectList.size}개의 발표가 있습니다",
                            style = Typography.titleMedium,
                            color = TextSecondary
                        )
                    }

                    // 리스트 영역만 스크롤
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        projectList.forEach { project ->
                            CommonList(
                                imagePainter = painterResource(id = R.drawable.img_list_practice),
                                title = project.projectName,
                                description = formatTimeOnly(project.projectDate),
                                onClick = {
                                    onDismiss()
                                    navController.navigate(NavRoutes.ProjectDetail.withId(project.projectId))
                                }
                            )
                        }

                    }

                    // 닫기 버튼
                    CommonButton(
                        text = "닫기",
                        size = ButtonSize.LG,
                        backgroundColor = BackgroundSecondary,
                        textColor = TextTertiary,
                        borderColor = BackgroundSecondary,
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

