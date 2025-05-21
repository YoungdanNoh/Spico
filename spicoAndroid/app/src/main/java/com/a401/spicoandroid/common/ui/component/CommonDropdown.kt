package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import com.a401.spicoandroid.common.ui.theme.*
import androidx.compose.ui.res.painterResource
import com.a401.spicoandroid.R

/**
 * @param text 메뉴에 표시될 텍스트
 * @param onClick 해당 메뉴 클릭 시 동작할 함수
 * @param icon 텍스트 왼쪽에 보여줄 아이콘 (선택 사항)
 * @param textColor 텍스트 색상 (기본값: TextPrimary)
 */
data class DropdownMenuItemData(
    val text: String,
    val onClick: () -> Unit,
    val icon: (@Composable () -> Unit)? = null,
    val textColor: Color = TextPrimary
)

/**
 * 공통 드롭다운 컴포넌트
 * @param expanded 메뉴가 펼쳐진 상태인지 여부
 * @param onDismissRequest 바깥 영역을 클릭했을 때 메뉴를 닫기 위한 콜백
 * @param menuItems 보여줄 메뉴 항목 리스트
 * @param modifier Modifier 확장
 */
@Composable
fun CommonDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    menuItems: List<DropdownMenuItemData>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        menuItems.forEach { item ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item.icon?.invoke()
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.text,
                            color = item.textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = item.onClick,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

/**
 * 프리뷰 예제
 */
@Preview(showBackground = true)
@Composable
fun CommonDropdownPreview() {
    var expanded by remember { mutableStateOf(true) }

    val menuItems = listOf(
        DropdownMenuItemData(
            "수정하기",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_black),
                    contentDescription = "Edit",
                    tint = TextPrimary
                )
            }, onClick = { }),
        DropdownMenuItemData(
            "프로젝트 삭제",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_black),
                    contentDescription = "Delete",
                    tint = Error)
            },
            textColor = Error,
            onClick = { })
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        CommonDropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            menuItems = menuItems
        )
    }
}
