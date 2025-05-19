package com.a401.spicoandroid.presentation.report.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme

@Composable
fun RandomReportScriptButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonButton(
        text = "음성 스크립트",
        size = ButtonSize.LG,
        onClick = onClick,
        modifier = modifier
    )
}
