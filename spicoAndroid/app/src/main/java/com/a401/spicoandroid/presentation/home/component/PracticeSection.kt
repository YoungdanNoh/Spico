package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeMode
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel


@Composable
fun PracticeSection(
    navController: NavHostController,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PracticeModeCard(
            title = "코칭 모드",
            imageRes = R.drawable.img_coaching_home,
            onClick = {
                viewModel.selectedMode = PracticeMode.COACHING
                navController.navigate(NavRoutes.ProjectSelect.route)
            },
            modifier = Modifier.weight(1f)
        )
        PracticeModeCard(
            title = "파이널 모드",
            imageRes = R.drawable.img_final_home,
            onClick = {
                viewModel.selectedMode = PracticeMode.FINAL
                navController.navigate(NavRoutes.ProjectSelect.route)
            },
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun PracticeModeCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(148f / 180f) // 화면 비율
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondary)
            .clickable(onClick = onClick)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Typography.displayMedium,
            color = TextPrimary
        )
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "$title 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
        )
    }
}

//@Preview(showBackground = true, widthDp = 360)
//@Composable
//fun PracticeSectionPreview() {
//    SpeakoAndroidTheme {
//        PracticeSection()
//    }
//}