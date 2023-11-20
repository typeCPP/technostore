package com.technostore.core.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import kotlin.math.floor
import kotlin.math.sqrt


object PhotoCompression {
    fun compress(context: Context, uri: Uri?): ByteArray? {
        if (uri == null) {
            return null
        }
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val out = ByteArrayOutputStream()
            val newBitmap = scaleBitmap(bitmap)
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            inputStream.close()
            return out.toByteArray()
        }
        return null
    }

    private fun scaleBitmap(input: Bitmap): Bitmap {
        val maxBytes = 1024 * 1024
        val currentWidth = input.width
        val currentHeight = input.height
        val currentPixels = currentWidth * currentHeight
        val maxPixels = maxBytes / 4
        if (currentPixels <= maxPixels) {
            return input
        }
        val scaleFactor = sqrt(maxPixels / currentPixels.toDouble())
        val newWidthPx = floor(currentWidth * scaleFactor).toInt()
        val newHeightPx = floor(currentHeight * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
    }
}