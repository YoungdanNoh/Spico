package com.a401.spicoandroid.presentation.report.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun VoiceScriptScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()

    // 임시 스크립트
    val scriptText = remember {
        """
        안녕하세요, 오늘은 AI 기술이 우리의 일상에 미치는 영향에 대해 이야기해보겠습니다.

        AI, 즉 인공지능은 이미 우리 삶 깊숙이 들어와 있습니다. 스마트폰의 음성 비서, 스트리밍 서비스의 추천 알고리즘, 심지어 온라인 쇼핑몰에서 보는 상품 추천까지 모두 AI의 결과물입니다.

        우리는 매일 AI가 분석한 데이터를 기반으로 보다 편리하고 맞춤화된 서비스를 경험하고 있습니다. 특히 최근 몇 년 사이, 자연어 처리 기술과 이미지 인식 기술의 발전은 눈부십니다. 챗봇을 통한 고객 서비스 자동화, 자율주행차량 기술, 개인 건강 모니터링 시스템까지 AI는 다양한 분야에서 인간의 삶을 지원하고 있습니다.

        이러한 변화는 일상의 작은 부분에서부터 산업 전반에 이르기까지 파급 효과를 만들어내고 있습니다.

        하지만 AI의 발전은 긍정적인 면만 있는 것은 아닙니다. 개인정보 보호 문제, 알고리즘 편향성, 일자리 변화 등 해결해야 할 과제들도 부분 존재합니다.            
        
        """.trimIndent()
    }

    BackHandler {
        navController.navigate(NavRoutes.FinalReport.route)
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "음성 스크립트",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.arrow_left,
                        contentDescription = "뒤로 가기",
                        onClick = {
                            navController.navigate(NavRoutes.FinalReport.route)
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
                        navController.navigate(NavRoutes.FinalReport.route)
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "자율 프로젝트 최종발표",
                style = Typography.displayMedium,
                color = TextPrimary
            )

            Text(
                text = scriptText,
                style = Typography.titleLarge,
                color = TextPrimary
            )
        }
    }
}
