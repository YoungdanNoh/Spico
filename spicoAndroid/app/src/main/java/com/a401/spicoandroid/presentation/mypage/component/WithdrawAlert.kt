package com.a401.spicoandroid.presentation.mypage.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun WithdrawAlert(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    CommonAlert(
        title = "회원 탈퇴가 됩니다.\n정말 탈퇴하시겠습니까?",
        confirmText = "탈퇴",
        confirmBackgroundColor = Error,
        confirmBorderColor     = Error,
        confirmTextColor = White,
        onConfirm = onConfirm,

        cancelText = "취소",
        cancelBackgroundColor = Placeholder,
        cancelBorderColor     = Placeholder,
        cancelTextColor = TextTertiary,
        onCancel = onDismiss,

        borderColor = White,
        onDismissRequest = onDismiss
    )
}