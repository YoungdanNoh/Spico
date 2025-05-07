package com.a401.spicoandroid.common.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun CommonBottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onFabClick: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val navItems = listOf(
        NavItem("home", R.drawable.ic_home_line, R.drawable.ic_home_filled, "홈"),
        NavItem("project_list", R.drawable.ic_presention_line, R.drawable.ic_presention_filled, "발표목록"),
        NavItem("random_speech", R.drawable.ic_random_line, R.drawable.ic_random_filled, "랜덤스피치"),
        NavItem("profile", R.drawable.ic_profile_line, R.drawable.ic_profile_filled, "프로필")
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            color = White,
            shadowElevation = 10.dp
        ) {}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.take(2).forEach { item ->
                BottomNavItem(
                    item = item,
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // FAB 공간

            navItems.takeLast(2).forEach { item ->
                BottomNavItem(
                    item = item,
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        CenterFab(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp),
            onClick = onFabClick // ✅ FAB 클릭 시 콜백 실행
        )
    }
}

@Composable
private fun BottomNavItem(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (selected) item.selectedIconRes else item.iconRes
    val tint = if (selected) Hover else TextTertiary

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = item.label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = item.label,
                style = Typography.titleMedium,
                color = tint,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun CenterFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(Disabled)
        )
        FloatingActionButton(
            onClick = onClick,
            containerColor = Action,
            contentColor = White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_white),
                contentDescription = "FAB"
            )
        }
    }
}

private data class NavItem(
    val route: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int,
    val label: String
)
