package com.songbird.qrmagic.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun saveQRCode(context: Context, bitmap: Bitmap) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "QRMagic"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = "QR_${System.currentTimeMillis()}.png"
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            launch(Dispatchers.Main) {
                Toast.makeText(context, "QR Code saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            launch(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save QR Code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
