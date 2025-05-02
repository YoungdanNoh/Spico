package com.a401.spicoandroid.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_thin,     FontWeight.Thin),
    Font(R.font.pretendard_extra_light,    FontWeight.ExtraLight),
    Font(R.font.pretendard_light,    FontWeight.Light),
    Font(R.font.pretendard_regular,  FontWeight.Normal),
    Font(R.font.pretendard_medium,   FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold,     FontWeight.Bold),
    Font(R.font.pretendard_extra_bold,FontWeight.ExtraBold),
    Font(R.font.pretendard_black,    FontWeight.Black)
)

val Typography = Typography(
    displayLarge = TextStyle(          // Semibold 24 · 24/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 24.sp
    ),
    displayMedium = TextStyle(         // Semibold 20 · 20/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 20.sp
    ),
    displaySmall = TextStyle(          // Semibold 16 · 16/18
        fontFamily = Pretendard,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 16.sp,
        lineHeight  = 18.sp
    ),

    headlineLarge = TextStyle(        // Semibold 16 · 16/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 16.sp
    ),
    headlineMedium = TextStyle(         // Semibold 12 · 12/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 12.sp
    ),
    headlineSmall = TextStyle(         // Regular 20 · 20/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 20.sp
    ),

    titleLarge = TextStyle(            // Regular   16 · 16/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 16.sp
    ),
    titleMedium = TextStyle(           // Regular   12 · 12/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 12.sp
    ),

    // Paragraph
    bodyLarge = TextStyle(             // Regular   16 · 16/24
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 16.sp,
        lineHeight  = 24.sp
    ),
    bodyMedium = TextStyle(            // Medium   24 · 24/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Medium,
        fontSize    = 24.sp
    ),
    bodySmall = TextStyle(             // Regular   12 · 12/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 12.sp
    ),

    // Caption
    labelSmall = TextStyle(            // Regular   12 · 12/Auto
        fontFamily = Pretendard,
        fontWeight  = FontWeight.Normal,
        fontSize    = 12.sp
    )
)