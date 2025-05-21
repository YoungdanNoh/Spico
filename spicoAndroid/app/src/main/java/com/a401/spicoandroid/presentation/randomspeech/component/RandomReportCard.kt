package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.common.utils.getTopicIconRes

@Composable
fun RandomReportCard(
    id: Int,
    topic: String,
    title: String,
    dateTime: String,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    CommonList(
        imagePainter = painterResource(id = getTopicIconRes(topic)),
        title = title,
        description = dateTime,
        onClick = { onClick(id) },
        onLongClick = { onLongClick(id) }
    )
}
@Preview(showBackground = true)
@Composable
fun RandomReportCardPreview() {
    SpeakoAndroidTheme {
        RandomReportCard(
            id = 1,
            topic = "economy",
            title = "MZ는 개복치다",
            dateTime = "2025.05.11 월요일",
            onClick = {},
            onLongClick = {}
        )
    }
}