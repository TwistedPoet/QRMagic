package com.songbird.qrmagic.ui

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class ThemeColorPair(
    val name: String,
    val foreground: Color,
    val background: Color
)

val qrThemes = listOf(
    ThemeColorPair("Classic", Color.Black, Color.White),
    ThemeColorPair("Invert", Color.White, Color.Black),
    ThemeColorPair("Midnight", Color(0xFF0D1B2A), Color(0xFFE0E1DD)),
    ThemeColorPair("Gold Label", Color(0xFFAA8800), Color(0xFFFFF8DC)),
    ThemeColorPair("Emerald", Color(0xFF014421), Color(0xFFE6F2EA)),
    ThemeColorPair("Cobalt", Color(0xFF002D72), Color(0xFFE5ECF6)),
    ThemeColorPair("Garnet", Color(0xFF7C0A02), Color(0xFFFBE9E7)),
    ThemeColorPair("Graphite", Color(0xFF2F4F4F), Color(0xFFF0F0F0)),
    ThemeColorPair("Twilight", Color(0xFF4B0082), Color(0xFFF3EFFF)),
    ThemeColorPair("Sunset", Color(0xFFC1440E), Color(0xFFFFF3E0))
)