package com.a401.spicoandroid.presentation.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import androidx.compose.foundation.pager.HorizontalPager

@Composable
fun LogoWithPagerSection(
    pagerState: PagerState,
    imageList: List<Int>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(BrokenWhite)
            .padding(top = 64.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_medium),
            contentDescription = "Spico Logo"
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            )
        }
    }
}
