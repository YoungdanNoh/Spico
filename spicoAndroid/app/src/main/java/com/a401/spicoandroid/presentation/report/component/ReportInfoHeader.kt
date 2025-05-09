package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ChipType
import com.a401.spicoandroid.common.ui.component.CommonChip

@Composable
fun ReportInfoHeader(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    projectTitle: String,
    roundCount: Int,
    chipText: String,
    chipType: ChipType
) {
    Surface(
        modifier = modifier
            .height(96.dp)
            .padding(16.dp),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column (modifier = Modifier.weight(1f)) {
                Text(
                    text = projectTitle,
                    style = Typography.displayMedium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CommonChip(text = chipText, type = chipType)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${roundCount}회차",
                        style = Typography.titleLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportInfoHeaderPreviewGroup() {
    Column(modifier = Modifier.padding(16.dp)) {
        ReportInfoHeader(
            imagePainter = painterResource(id = R.drawable.img_coaching_practice),
            projectTitle = "관통 프로젝트",
            roundCount = 3,
            chipText = "코칭모드",
            chipType = ChipType.REPORT_ACTION
        )

        ReportInfoHeader(
            imagePainter = painterResource(id = R.drawable.img_final_practice),
            projectTitle = "자율 프로젝트",
            roundCount = 5,
            chipText = "파이널모드",
            chipType = ChipType.REPORT_ERROR
        )
    }
}

