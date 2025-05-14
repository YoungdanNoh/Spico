package com.a401.spicoandroid.common.ui.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun ExitConfirmDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    CommonAlert(
        title = "연습 시간이 짧아\n리포트가 생성되지 않습니다.\n정말 종료하시겠습니까?",
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
        onDismissRequest = onDismiss
    )
}

