package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun RandomReportDeleteAlert(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    CommonAlert(
        title = "리포트가 삭제됩니다.\n정말 삭제하시겠습니까?",
        confirmText = "삭제",
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