package com.a401.spicoandroid.common.ui.component
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.unit.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.foundation.shape.RoundedCornerShape
import com.a401.spicoandroid.common.ui.theme.*


enum class ButtonSize(val height: Dp, val width: Dp, val fontSize: TextUnit) {
  XS(height = 32.dp, width = 48.dp, fontSize = 12.sp), // AppBar 버튼
  SM(height = 40.dp, width = 76.dp, fontSize = 16.sp), // 종료 버튼
  MD(height = 40.dp, width = 132.dp, fontSize = 16.sp), // 알림창 버튼
  LG(height = 40.dp, width = 328.dp, fontSize = 16.sp), // 다음 버튼
  XL(height = 64.dp, width = 180.dp, fontSize = 20.sp), 
}

@Composable
fun CommonButton(
    text: String,
    size: ButtonSize = ButtonSize.MD,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Action,
    textColor: Color = White,
    borderRadius: Dp = 8.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(size.height)
            .width(size.width)
            .clip(RoundedCornerShape(borderRadius))
            .background(
                color = if (enabled) backgroundColor else Disabled
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = if (enabled) textColor else TextTertiary,
                fontSize = size.fontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

