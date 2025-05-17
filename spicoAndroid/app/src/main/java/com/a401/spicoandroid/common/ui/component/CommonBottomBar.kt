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
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun CommonBottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onFabClick: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // 홀화면에서 진입하는 리포트
    val source = backStackEntry?.arguments?.getString("source")

    val navItems = listOf(
        NavItem(
            route = NavRoutes.Home.route,
            matchingRoutes = listOf(NavRoutes.Home.route),
            iconRes = R.drawable.ic_home_line,
            selectedIconRes = R.drawable.ic_home_filled,
            label = "홈"
        ),
        NavItem(
            route = NavRoutes.ProjectList.route,
            matchingRoutes = listOf(
                NavRoutes.ProjectList.route,
                "project_detail",
                NavRoutes.CoachingReport.route,
                NavRoutes.FinalReport.route,
                "home_coaching_report",
                "home_final_report"
            ),
            iconRes = R.drawable.ic_presention_line,
            selectedIconRes = R.drawable.ic_presention_filled,
            label = "발표목록"
        ),
        NavItem(
            route = NavRoutes.RandomSpeechLanding.route,
            matchingRoutes = listOf(
                NavRoutes.RandomSpeechLanding.route,
                NavRoutes.RandomSpeechList.route
            ),
            iconRes = R.drawable.ic_random_line,
            selectedIconRes = R.drawable.ic_random_filled,
            label = "랜덤스피치"
        ),
        NavItem(
            route = NavRoutes.Profile.route,
            matchingRoutes = listOf(NavRoutes.Profile.route),
            iconRes = R.drawable.ic_profile_line,
            selectedIconRes = R.drawable.ic_profile_filled,
            label = "프로필"
        )
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
                    selected = item.matchingRoutes.any { match ->
                        currentRoute?.split("/")?.firstOrNull() == match
                    },
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // FAB 공간

            navItems.takeLast(2).forEach { item ->
                BottomNavItem(
                    item = item,
                    selected = item.matchingRoutes.any { match ->
                        currentRoute?.split("/")?.firstOrNull() == match
                    },
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        CenterFab(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp),
            onClick = onFabClick
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
    val matchingRoutes: List<String>,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int,
    val label: String
)
