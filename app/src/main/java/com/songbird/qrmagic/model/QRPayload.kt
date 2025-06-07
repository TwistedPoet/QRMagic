package com.songbird.qrmagic.model

import android.graphics.Bitmap
import com.songbird.qrmagic.util.QRCodeStylingOptions

data class QRPayload(
    val content: String,
    val label: String?,
    val styling: QRCodeStylingOptions,
    val icon: String?,
    val qrBitmap: Bitmap
)
