package com.a401.spicoandroid.common.ui.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.theme.*

/**
 * 30초 미만 발표 시 리포트가 생성되지 않는다는 안내를 보여주는 다이얼로그 컴포넌트.
 *
 * - 사용자가 발표 도중 종료를 선택했을 때,
 *   경과 시간이 30초 미만이면 리포트가 생성되지 않음을 안내
 *
 * @param onConfirm "종료" 버튼 클릭 시 호출되는 콜백
 * @param onCancel "취소" 버튼 클릭 시 호출되는 콜백
 * @param onDismiss 다이얼로그 외부를 클릭하거나 시스템에 의해 닫힐 때 호출되는 콜백
 */

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

