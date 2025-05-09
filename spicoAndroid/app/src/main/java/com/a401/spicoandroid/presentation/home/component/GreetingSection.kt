package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun GreetingSection(
    username: String = "사용자",
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrokenWhite),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints {
            val screenWidth = maxWidth
            val contentWidth = if (screenWidth < 360.dp) screenWidth else 360.dp

            Column(
                modifier = Modifier
                    .width(contentWidth)
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrokenWhite)
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_small),
                    contentDescription = "앱 로고",
                    modifier = Modifier
                        .height(28.dp)
                        .padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "반가워요!",
                            style = Typography.titleLarge,
                            color = TextTertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                val trimmedUsername = if (username.length >= 10) {
                                    username.take(8) + "..."
                                } else {
                                    username
                                }
                                withStyle(style = SpanStyle(color = Hover)) {
                                    append(trimmedUsername)
                                }
                                withStyle(style = SpanStyle(color = TextPrimary)) {
                                    append("님,")
                                }
                            },
                            style = Typography.bodyMedium.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            maxLines = 1,
                            softWrap = false,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "오늘도 연습해요",
                            style = Typography.displayLarge.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 36.sp,
                                letterSpacing = 0.04.em
                            ),
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        CommonButton(
                            text = "프로젝트 만들기",
                            size = ButtonSize.MD,
                            backgroundColor = Action,
                            borderColor = Action,
                            textColor = White,
                            onClick = {
                                navController.navigate(NavRoutes.ProjectCreate.route)
                            }
                        )

                        CommonButton(
                            text = "프로젝트 목록",
                            size = ButtonSize.MD,
                            backgroundColor = Action,
                            borderColor = Action,
                            textColor = White,
                            onClick = {
                                navController.navigate(NavRoutes.ProjectList.route)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.character_home_1),
                        contentDescription = "캐릭터 이미지",
                        modifier = Modifier.size(148.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true,widthDp = 412)
//@Composable
//fun GreetingSectionPreview() {
//    val fakeNavController = LocalNavController.current
//    SpeakoAndroidTheme {
//        GreetingSection(navController = fakeNavController)
//    }
//}

