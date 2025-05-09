package com.a401.spicoandroid.presentation.home.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.a401.spicoandroid.common.ui.theme.LineTertiary
import com.a401.spicoandroid.common.ui.theme.TextSecondary
import com.a401.spicoandroid.common.ui.theme.TextTertiary

@Composable
fun HomeFooterSection() {
    val context = LocalContext.current

    fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    val teamMembers = listOf(
        "노영단" to "https://github.com/YoungdanNoh",
        "강명주" to "https://github.com/notrealsilk",
        "김서현" to "https://github.com/seohye-ki",
        "김예진" to "https://github.com/z5zH0",
        "신유영" to "https://github.com/shinyou28",
        "이승연" to "http://github.com/leesyseel"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(
            thickness = 0.7.dp,
            color = LineTertiary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Text(
            text = "© 2025 Spico. SSAFY 12기 자율 A401",
            color = TextTertiary,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))

        // 팀원 이름
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            teamMembers.forEachIndexed { index, (name, url) ->
                FooterLink(name = name, url = url, onClick = ::openLink)
                if (index < teamMembers.lastIndex) {
                    FooterSeparator()
                }
            }
        }
    }
}

@Composable
fun FooterLink(name: String, url: String, onClick: (String) -> Unit) {
    Text(
        text = name,
        color = TextTertiary,
        fontSize = 9.sp,
        textAlign = TextAlign.Center,
        style = TextStyle(textDecoration = TextDecoration.None),
        modifier = Modifier
            .clickable { onClick(url) }
            .padding(horizontal = 2.dp)
    )
}

@Composable
fun FooterSeparator() {
    Text(
        text = "·",
        color = TextTertiary,
        fontSize = 9.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 2.dp)
    )
}
