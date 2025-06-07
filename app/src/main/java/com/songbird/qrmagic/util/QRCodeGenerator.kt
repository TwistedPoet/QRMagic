package com.songbird.qrmagic.util

import android.graphics.*
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

fun createQRCode(
    text: String,
    stylingOptions: QRCodeStylingOptions
): Bitmap {
    if (text.isBlank()) {
        // Return 1x1 pixel as safe fallback if called accidentally
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    val size = 1024
    val hints = mapOf(EncodeHintType.MARGIN to 1)

    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        text,
        BarcodeFormat.QR_CODE,
        size,
        size,
        hints
    )

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            val color = if (bitMatrix[x, y])
                stylingOptions.qrForegroundColor.toArgb()
            else
                stylingOptions.qrBackgroundColor.toArgb()
            bitmap.setPixel(x, y, color)
        }
    }

    return if (stylingOptions.borderEnabled) {
        addBorderToBitmap(bitmap, stylingOptions)
    } else {
        bitmap
    }
}

private fun addBorderToBitmap(
    bitmap: Bitmap,
    options: QRCodeStylingOptions
): Bitmap {
    val border = options.borderWidthPx
    val borderedBitmap = Bitmap.createBitmap(
        bitmap.width + border * 2,
        bitmap.height + border * 2,
        bitmap.config
    )

    val canvas = Canvas(borderedBitmap)
    val paint = Paint().apply {
        color = options.borderColor.toArgb()
        style = Paint.Style.FILL
    }

    canvas.drawRect(
        0f, 0f,
        borderedBitmap.width.toFloat(),
        borderedBitmap.height.toFloat(),
        paint
    )

    canvas.drawBitmap(bitmap, border.toFloat(), border.toFloat(), null)

    return borderedBitmap
}

fun attachLabelToBitmap(
    qrBitmap: Bitmap,
    stylingOptions: QRCodeStylingOptions
): Bitmap {
    val label = stylingOptions.labelText
    val icon = stylingOptions.labelIcon

    if (label.isBlank()) {
        return qrBitmap // No label to draw
    }

    val labelTextColor = stylingOptions.qrBackgroundColor.toArgb()
    val labelBgColor = stylingOptions.qrForegroundColor.toArgb()
    val cornerRadius = stylingOptions.cornerRadiusDp

    val width = qrBitmap.width
    val padding = 32
    val textSize = 64f

    val textPaint = Paint().apply {
        isAntiAlias = true
        color = labelTextColor
        this.textSize = textSize
        textAlign = Paint.Align.LEFT
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }

    val iconPaint = Paint(textPaint).apply {
        textAlign = Paint.Align.LEFT
    }

    val labelTextWidth = textPaint.measureText(label)
    val iconWidth = icon?.let { iconPaint.measureText(it) } ?: 0f
    val fm = textPaint.fontMetrics
    val textHeight = (fm.bottom - fm.top).toInt()
    val totalHeight = qrBitmap.height + textHeight + padding * 2
    val finalBitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(finalBitmap)

    // Draw QR code
    canvas.drawBitmap(qrBitmap, 0f, 0f, null)

    // Draw background capsule or rect
    val bgPaint = Paint().apply {
        isAntiAlias = true
        color = labelBgColor
        style = Paint.Style.FILL
    }

    val labelTop = qrBitmap.height + padding.toFloat()
    val labelBottom = labelTop + textHeight + padding.toFloat()

    if (stylingOptions.useCapsuleStyle) {
        canvas.drawRoundRect(
            0f,
            labelTop,
            width.toFloat(),
            labelBottom,
            cornerRadius,
            cornerRadius,
            bgPaint
        )
    } else {
        canvas.drawRect(
            0f,
            labelTop,
            width.toFloat(),
            labelBottom,
            bgPaint
        )
    }

    // Draw icon and label centered
    val spacing = 32f
    val baselineY = labelTop + (labelBottom - labelTop) / 2 - (textPaint.descent() + textPaint.ascent()) / 2

    val totalTextWidth = (icon?.let { iconPaint.measureText(it) + spacing } ?: 0f) + labelTextWidth
    val startX = width / 2f - totalTextWidth / 2

    var currentX = startX
    icon?.let {
        canvas.drawText(it, currentX, baselineY, iconPaint)
        currentX += iconPaint.measureText(it) + spacing
    }

    canvas.drawText(label, currentX, baselineY, textPaint)


    return finalBitmap
}