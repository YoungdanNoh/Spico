package com.a401.spicoandroid.common.ui.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun DeleteModalBottomSheet(
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BaseModalBottomSheet(onDismissRequest = onDismissRequest) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onDeleteClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_trash_error),
                    contentDescription = "삭제 아이콘",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "삭제하기",
                style = Typography.headlineSmall,
                color = Error,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
fun DeleteModalBottomSheetPreview_DesignOnly() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = White,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // 드래그 핸들
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(LineTertiary)
                )
            }

            // 삭제하기 row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_trash_error),
                        contentDescription = "삭제 아이콘",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "삭제하기",
                    style = Typography.headlineSmall,
                    color = Error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
