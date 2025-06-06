package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.a401.spicoandroid.common.ui.theme.*

/**
 * 공통 Alert 컴포넌트 (확인/취소 팝업 다이얼로그)
 *
 * @param title 다이얼로그 내부에 표시할 메시지 텍스트
 * @param confirmText 확인 버튼에 표시할 텍스트
 * @param onConfirm 확인 버튼 클릭 시 동작할 함수
 * @param confirmTextColor 확인 버튼 텍스트 색상 (기본값: White)
 * @param confirmBackgroundColor 확인 버튼 배경색 (기본값: Action)
 * @param confirmBorderColor 확인 버튼 테두리 색 (기본값: Action)
 * @param cancelText 취소 버튼에 표시할 텍스트
 * @param onCancel 취소 버튼 클릭 시 동작할 함수
 * @param cancelTextColor 취소 버튼 텍스트 색상 (기본값: TextTertiary)
 * @param cancelBackgroundColor 취소 버튼 배경색 (기본값: BackgroundSecondary)
 * @param cancelBorderColor 취소 버튼 테두리 색상 (기본값: BackgroundSecondary)
 * @param borderColor 다이얼로그 전체 테두리 색상 (기본값: White)
 * @param onDismissRequest 바깥 영역 클릭 시 다이얼로그 닫힘 처리 콜백
 */

@Composable
fun CommonAlert(
    modifier: Modifier = Modifier,
    title: String,
    confirmText: String,
    onConfirm: () -> Unit,
    confirmTextColor: Color = White,
    confirmBackgroundColor: Color = Action,
    confirmBorderColor: Color = Action,
    cancelText: String,
    onCancel: () -> Unit,
    cancelTextColor: Color = TextTertiary,
    cancelBackgroundColor: Color = BackgroundSecondary,
    cancelBorderColor: Color = BackgroundSecondary,
    borderColor: Color = White,
    onDismissRequest: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OverlayDark20)
                .clickable(onClick = onDismissRequest)
                .padding(16.dp)
        ) {
            Box(
                modifier = modifier
                    .width(312.dp)
                    .wrapContentHeight()
                    .align(Alignment.Center)
                    .background(White, RoundedCornerShape(8.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CommonButton(
                            text = cancelText,
                            onClick = onCancel,
                            size = ButtonSize.MD,
                            textColor = cancelTextColor,
                            backgroundColor = cancelBackgroundColor,
                            borderColor = cancelBorderColor,
                            modifier = Modifier.weight(1f)
                        )
                        CommonButton(
                            text = confirmText,
                            onClick = onConfirm,
                            size = ButtonSize.MD,
                            textColor = confirmTextColor,
                            backgroundColor = confirmBackgroundColor,
                            borderColor = confirmBorderColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonAlertPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonAlert(
            title = "리포트가 삭제됩니다.\n정말 삭제하시겠습니까?",
            confirmText = "삭제",
            onConfirm = {},
            confirmTextColor = White,
            confirmBackgroundColor = Error,
            confirmBorderColor = Error,
            cancelText = "취소",
            onCancel = {},
            cancelTextColor = TextTertiary,
            cancelBackgroundColor = BackgroundSecondary,
            cancelBorderColor = BackgroundSecondary,
            borderColor = White,
            onDismissRequest = {}
        )
    }
}
