package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.getTopicIconRes
import com.a401.spicoandroid.common.utils.getTopicKor

@Composable
fun TopicItem(
    topic: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconRes = getTopicIconRes(topic)
    val topicKor = getTopicKor(topic)
    val borderColor = if (isSelected) Action else LineTertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = topicKor,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = topicKor,
            style = Typography.titleLarge,
            color = TextPrimary
        )
    }
}
