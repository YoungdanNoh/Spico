package com.a401.spicoandroid.common.ui.component
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.a401.spicoandroid.R

/**
 * 버튼 크기(Enum) 정의
 *
 * @param height 버튼 높이
 * @param width 버튼 너비
 * @param fontSize 버튼 내부 텍스트 크기
 */
enum class ButtonSize(val height: Dp, val width: Dp?, val textStyle: TextStyle) {
    XS(height = 32.dp, width = 48.dp, textStyle = Typography.labelMedium),
    SM(height = 40.dp, width = 76.dp, textStyle = Typography.displaySmall),
    MD(height = 40.dp, width = 132.dp, textStyle = Typography.displaySmall),
    LG(height = 40.dp, width = null, textStyle = Typography.displaySmall), // fillMaxWidth
    XL(height = 64.dp, width = 180.dp, textStyle = Typography.displaySmall),
}

/**
 * 공통 사각형 버튼 컴포넌트
 *
 * @param modifier        추가 Modifier 확장
 * @param text            버튼에 표시될 텍스트
 * @param size            버튼 크기 (ButtonSize Enum)
 * @param enabled         버튼 활성화 여부 (false 시 클릭 비활성화)
 * @param backgroundColor 활성화 상태일 때 버튼 배경색
 * @param borderColor     활성화 상태일 때 버튼 테두리 색
 * @param textColor       활성화 상태일 때 텍스트 색상
 * @param disabledBackgroundColor 비활성화 상태일 때 배경색 (null이면 기본 Disabled 색 사용)
 * @param disabledBorderColor     비활성화 상태일 때 테두리 색 (null이면 기본 Disabled 색 사용)
 * @param disabledTextColor       비활성화 상태일 때 텍스트 색 (null이면 기본 TextTertiary 사용)
 * @param borderRadius    버튼 모서리 둥글기
 * @param onClick         버튼 클릭 시 동작할 함수
 */
@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String,
    size: ButtonSize = ButtonSize.MD,
    enabled: Boolean = true,
    backgroundColor: Color = Action,
    borderColor: Color = Action,
    textColor: Color = White,
    disabledBackgroundColor: Color? = null,
    disabledBorderColor: Color? = null,
    disabledTextColor: Color? = null,
    borderRadius: Dp = 8.dp,
    onClick: () -> Unit,
) {
    val widthModifier = size.width?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth()

    val bgColor = if (enabled) backgroundColor else disabledBackgroundColor ?: Disabled
    val bdColor = if (enabled) borderColor else disabledBorderColor ?: Disabled
    val txtColor = if (enabled) textColor else disabledTextColor ?: TextTertiary

    Box(
        modifier = modifier
            .then(widthModifier)
            .height(size.height)
            .clip(RoundedCornerShape(borderRadius))
            .border(
                width = 1.dp,
                color = bdColor,
                shape = RoundedCornerShape(borderRadius)
            )
            .background(bgColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = size.textStyle.copy(color = txtColor)
        )
    }
}

/**
 * 공통 원형 아이콘 버튼 컴포넌트
 *
 * @param icon 아이콘 컴포저블 (예: Icon(...) )
 * @param size 버튼 지름 (기본값: 48.dp)
 * @param backgroundColor 배경색
 * @param enabled 버튼 활성화 여부
 * @param onClick 클릭 시 동작할 함수
 * @param modifier 추가 Modifier 확장
 */
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
@Composable
fun CommonIconTextButton(
    modifier: Modifier = Modifier,
    iconResId: Int,
    text: String,
    size: ButtonSize = ButtonSize.LG,
    enabled: Boolean = true,
    backgroundColor: Color = Action,
    borderColor: Color = Action,
    textColor: Color = White,
    iconTint: Color = White,
    borderRadius: Dp = 8.dp,
    onClick: () -> Unit,
) {
    val widthModifier = size.width?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth()
    Box(
        modifier = modifier
            .then(widthModifier)
            .height(size.height)
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
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "아이콘",
                modifier = Modifier.size(20.dp),
                tint = if (enabled) iconTint else TextTertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = size.textStyle.copy(
                    color = if (enabled) textColor else TextTertiary
                )
            )
        }
    }
}
@Composable
fun LargeIconCircleButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    size: Dp = 86.dp,
    borderWidth: Dp = 6.dp,
    borderColor: Color = Action,
    backgroundColor: Color = White,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}


/**
 * 프리뷰 예제
 */
@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
  Column (
    verticalArrangement = Arrangement.spacedBy(12.dp),
    modifier = Modifier.padding(16.dp)
  ) {
    // 소
    CommonButton(
      text = "편집",
      backgroundColor = White,
      borderColor = Action,
      textColor = Action,
      size = ButtonSize.XS,
      onClick = {},
    )
    // 중소
    CommonButton(
      text = "대본",
      backgroundColor = White,
      borderColor = Action,
      textColor = Action,
      size = ButtonSize.SM,
      onClick = {},
    )
    // 중대
    CommonButton(
      text = "취소",
      backgroundColor = BackgroundSecondary,
      borderColor = BackgroundSecondary,
      textColor = TextTertiary,
      size = ButtonSize.MD,
      onClick = {},
    )
    // 대
    CommonButton(
      text = "다음",
      size = ButtonSize.LG,
      onClick = {},
    )
      // 대 (아이콘)
      CommonIconTextButton(
          iconResId = R.drawable.ic_add_white,
          text = "새 프로젝트 등록하기",
          size = ButtonSize.LG,
          onClick = { /* 클릭 테스트 */ }
      )
    // 원형
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
// 큰 원형
@Preview(showBackground = true)
@Composable
fun LargeIconCircleButtonPreview() {
    LargeIconCircleButton(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_microphone_white),
                contentDescription = "Trash",
                tint = White,
                modifier = Modifier.size(32.dp) // 아이콘 크기 조절 가능
            )
        },
        borderColor = Disabled,
        backgroundColor = Action,
        onClick = { }
    )
}
