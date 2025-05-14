package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.getTopicIconRes
import com.a401.spicoandroid.common.utils.getTopicKor
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechTopic

@Composable
fun TopicItem(
    topic: RandomSpeechTopic,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconRes = getTopicIconRes(topic.name)
    val topicKor = getTopicKor(topic.name)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .dropShadow1()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = topicKor,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = topicKor,
            style = Typography.displayMedium,
            color = TextPrimary
        )
    }
}
