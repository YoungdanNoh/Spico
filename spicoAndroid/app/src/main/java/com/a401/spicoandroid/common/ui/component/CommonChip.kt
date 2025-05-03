package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

enum class ChipType {
    REPORT_ACTION,
    REPORT_ERROR,
    MODE_SELECT
}

@Composable
fun CommonChip(
    text: String,
    type: ChipType,
    modifier: Modifier = Modifier
) {
    val backgroundColor: Color
    val contentColor: Color
    val borderStroke: BorderStroke?
    val textStyle: TextStyle

    when (type) {
        ChipType.REPORT_ACTION -> {
            backgroundColor = Action
            contentColor = White
            borderStroke = null
            textStyle = MaterialTheme.typography.headlineMedium
        }
        ChipType.REPORT_ERROR -> {
            backgroundColor = Error
            contentColor = White
            borderStroke = null
            textStyle = MaterialTheme.typography.headlineMedium
        }
        ChipType.MODE_SELECT -> {
            backgroundColor = Color.Transparent
            contentColor = TextSecondary
            borderStroke = BorderStroke(1.dp, LineTertiary)
            textStyle = MaterialTheme.typography.labelSmall
        }
    }

    Surface(
        modifier = modifier
            .width(72.dp)
            .height(24.dp),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = borderStroke
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = textStyle,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonChips() {
    SpeakoAndroidTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonChip(text = "코칭모드", type = ChipType.REPORT_ACTION)
            CommonChip(text = "파이널모드", type = ChipType.REPORT_ERROR)
            CommonChip(text = "#대본없음", type = ChipType.MODE_SELECT)
        }
    }
}
