package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.a401.spicoandroid.common.ui.bottomsheet.DeleteModalBottomSheet
import com.a401.spicoandroid.common.ui.theme.OverlayDark20

@Composable
fun RandomReportDeleteBottomSheet(
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OverlayDark20)
    ) {
        DeleteModalBottomSheet(
            onDeleteClick = onDeleteClick,
            onDismissRequest = onDismissRequest
        )
    }
}