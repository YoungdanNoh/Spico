package com.a401.spicoandroid.common.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptBottomSheet(
    scripts: List<String>,
    onScriptClick: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = White,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(LineTertiary)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 404.dp)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 28.dp)
        ) {
            scripts.forEachIndexed { index, script ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundPrimary)
                        .clickable { onScriptClick(index) }
                        .padding(16.dp)
                ) {
                    Text(
                        text = script,
                        style = Typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}


@Preview(name = "초기 높이 바텀시트", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
fun ScriptBottomSheetCollapsedPreview() {
    val dummyScripts = listOf(
        "안녕하세요, 오늘은 AI 기술이 우리의 일상에 미치는 영향에 대해 이야기해보겠습니다.",
        "AI, 즉 인공지능은 이미 우리 삶 깊숙이 들어와 있습니다...",
        "우리는 매일 AI가 분석한 데이터를 기반으로 보다 편리한 서비스를 경험하고 있습니다."
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(404.dp)
            .background(color = White, shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .padding(horizontal = 12.dp)
    ) {
        // 드래그 핸들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(LineTertiary)
            )
        }

        // 스크립트 리스트
        dummyScripts.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundPrimary)
                    .padding(16.dp)
            ) {
                Text(
                    text = it,
                    style = Typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}

@Preview(name = "전체 높이 바텀시트", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
fun ScriptBottomSheetExpandedPreview() {
    val dummyScripts = List(10) { i ->
        "${i + 1}. 이건 예시 스크립트입니다. 바텀시트가 확장된 상태에서 여러 줄이 보입니다."
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp) // 일반 스마트폰 기준 최대 높이 근사값
            .background(color = White, shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(LineTertiary)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            dummyScripts.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundPrimary)
                        .padding(16.dp)
                ) {
                    Text(
                        text = it,
                        style = Typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

