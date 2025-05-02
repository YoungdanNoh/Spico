package com.a401.spicoandroid.common.ui.component
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.theme.*
import androidx.compose.ui.res.painterResource
import com.a401.spicoandroid.R


enum class ButtonSize(val height: Dp, val width: Dp, val fontSize: TextUnit) {
  XS(height = 32.dp, width = 48.dp, fontSize = 12.sp), // AppBar 버튼
  SM(height = 40.dp, width = 76.dp, fontSize = 16.sp), // 종료 버튼
  MD(height = 40.dp, width = 132.dp, fontSize = 16.sp), // 알림창 버튼
  LG(height = 40.dp, width = 328.dp, fontSize = 16.sp), // 다음 버튼
  XL(height = 64.dp, width = 180.dp, fontSize = 20.sp), 
}

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String,
    size: ButtonSize = ButtonSize.MD,
    enabled: Boolean = true,
    backgroundColor: Color = Action,
    borderColor: Color = Action,
    textColor: Color = White,
    borderRadius: Dp = 8.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(size.height)
            .width(size.width)
            .clip(RoundedCornerShape(borderRadius))
            .border(
                width = 1.dp,
                color = if (enabled) borderColor else Disabled,
                shape = RoundedCornerShape(borderRadius)
            )
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

@Composable
fun IconCircleButton(
  modifier: Modifier = Modifier,
  icon: @Composable () -> Unit,
  size: Dp = 48.dp,
  backgroundColor: Color = Action,
  enabled: Boolean = true,
  onClick: () -> Unit,
) {
  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(backgroundColor)
      .clickable(enabled = enabled, onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    icon()
  }
}



@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
  Column (
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.padding(16.dp)
  ) {
    CommonButton(
      text = "편집",
      backgroundColor = White,
      borderColor = Action,
      textColor = Action,
      size = ButtonSize.XS,
      onClick = {},
    )
    CommonButton(
      text = "대본",
      backgroundColor = White,
      borderColor = Action,
      textColor = Action,
      size = ButtonSize.SM,
      onClick = {},
    )
    CommonButton(
      text = "취소",
      backgroundColor = BackgroundSecondary,
      borderColor = BackgroundSecondary,
      textColor = TextTertiary,
      size = ButtonSize.MD,
      onClick = {},
    )
    CommonButton(
      text = "다음",
      size = ButtonSize.LG,
      onClick = {},
    )
    IconCircleButton(
      icon = { 
        Icon(
          painter = painterResource(id = R.drawable.ic_trash_white),
          contentDescription = "Trash",
          tint = White,     
        )
      }, 
      backgroundColor = Error,
      enabled = true,
      onClick = {},
    )
  }
}
