package com.a401.spicoandroid.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun LoginDescriptionText(
    currentPage: Int,
    textList: List<List<Pair<String, Color>>>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        val richText = buildAnnotatedString {
            textList[currentPage].forEach { (text, color) ->
                withStyle(Typography.displayMedium.copy(color = color).toSpanStyle()) {
                    append(text)
                }
            }
        }

        Text(
            text = richText,
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
