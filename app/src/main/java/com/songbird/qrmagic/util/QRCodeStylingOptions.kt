package com.songbird.qrmagic.util

import androidx.compose.ui.graphics.Color

data class QRCodeStylingOptions(
    val labelText: String = "",
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Black,
    val useCapsuleStyle: Boolean = true,
    val borderEnabled: Boolean = false,
    val borderColor: Color = Color.Black,
    val borderWidthPx: Int = 4,
    val cornerRadiusDp: Float = 20f,

    // QR Code coloring
    val qrForegroundColor: Color = Color.Black,
    val qrBackgroundColor: Color = Color.White,

    // Optional icon (e.g. emoji or small symbol)
    val labelIcon: String? = null
)