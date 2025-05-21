package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.theme.BackgroundSecondary
import com.a401.spicoandroid.common.ui.theme.Error
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.White

/**
 * 랜덤스피치 종료 확인 다이얼로그
 *
 * @param onDismissRequest 바깥 영역 클릭 또는 취소 시 호출
 * @param onConfirm 종료 확정 시 호출 (예: 랜딩 페이지 이동 등)
 * @param onCancel 취소 시 호출
 */
@Composable
fun RandomSpeechExitAlert(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    CommonAlert(
        title = "랜덤스피치를 종료하시겠습니까?",
        confirmText = "종료",
        onConfirm = onConfirm,
        confirmTextColor = White,
        confirmBackgroundColor = Error,
        confirmBorderColor = Error,
        cancelText = "취소",
        onCancel = onCancel,
        cancelTextColor = TextTertiary,
        cancelBackgroundColor = BackgroundSecondary,
        cancelBorderColor = BackgroundSecondary,
        borderColor = White,
        onDismissRequest = onDismissRequest
    )
}
