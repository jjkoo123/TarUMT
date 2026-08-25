package com.example.gigmatch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Navy = Color(0xFF0E1E33)
val BrandBlue = Color(0xFF0068C9)
val DeepBlue = Color(0xFF004487)
val SuccessGreen = Color(0xFF087A3E)
val BrightGreen = Color(0xFF4BE477)
val AppBackground = Color(0xFFF7F8FE)
val CardBorder = Color(0xFFC9DEFF)
val SoftBlue = Color(0xFFE7F0FF)
val MutedText = Color(0xFF4A5260)

private val GigMatchColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DeepBlue,
    secondary = SuccessGreen,
    onSecondary = Color.White,
    background = AppBackground,
    onBackground = Navy,
    surface = Color.White,
    onSurface = Navy,
    outline = CardBorder,
)

private val GigMatchTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 21.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 23.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
    ),
)

@Composable
fun GigMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GigMatchColors,
        typography = GigMatchTypography,
        content = content,
    )
}
