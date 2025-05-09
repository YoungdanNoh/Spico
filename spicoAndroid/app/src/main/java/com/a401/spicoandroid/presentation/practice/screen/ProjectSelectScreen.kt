package com.a401.spicoandroid.presentation.practice.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.presentation.practice.dummy.DummyProjectList

@Composable
fun ProjectSelectScreen(
    navController: NavController,
    mode: String // "coaching" or "final"
) {
    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 선택",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = { navController.popBackStack() }
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonIconTextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    iconResId = R.drawable.ic_add_white,
                    text = "새 프로젝트 등록하기",
                    size = ButtonSize.LG,
                    onClick = { /* TODO: 새 프로젝트 등록 이동 */ }
                )
            }
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DummyProjectList.forEach { project ->
                        CommonList(
                            modifier = Modifier.dropShadow1(),
                            imagePainter = painterResource(id = R.drawable.img_list_practice),
                            title = project.projectName,
                            description = formatDateWithDay(project.projectDate),
                            onClick = {
                                when (mode) {
                                    "final" -> navController.navigate("final_setting")
                                    "coaching" -> {// TODO: 코칭 모드 시작 화면 연결
                                    }
                                }
                            }
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FAF8, widthDp = 360)
@Composable
fun PreviewProjectSelectScreen() {
    SpeakoAndroidTheme {
        ProjectSelectScreen(
            navController = rememberNavController(),
            mode = "final"
        )
    }
}
