package com.medseen.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.util.UUID
import kotlin.math.roundToInt

/**
 * Saves a picked gallery image into app-private storage, resized and compressed
 * so large photos still fit doctor portrait slots cleanly.
 */
object ImageStorage {

    /** Longest side after resize (px). Keeps faces sharp in list cards without huge files. */
    const val MAX_DIMENSION_PX = 1024

    /** JPEG quality for saved portraits. */
    const val JPEG_QUALITY = 85

    fun savePickedImage(context: Context, uri: Uri): String {
        val photosDir = File(context.filesDir, "photos").apply { mkdirs() }
        val dest = File(photosDir, "${UUID.randomUUID()}.jpg")

        val original = decodeBitmap(context, uri)
            ?: error("Unable to read selected image")
        val oriented = applyExifOrientation(context, uri, original)
        if (oriented !== original) original.recycle()

        val resized = resizeToMaxDimension(oriented, MAX_DIMENSION_PX)
        if (resized !== oriented) oriented.recycle()

        dest.outputStream().use { out ->
            resized.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
        }
        resized.recycle()
        return dest.absolutePath
    }

    private fun decodeBitmap(context: Context, uri: Uri): Bitmap? {
        // Bound decode to avoid OOM on very large camera photos.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

        var sampleSize = 1
        val longest = maxOf(bounds.outWidth, bounds.outHeight).coerceAtLeast(1)
        while (longest / sampleSize > MAX_DIMENSION_PX * 2) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    private fun applyExifOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val orientation = context.contentResolver.openInputStream(uri)?.use { stream ->
            ExifInterface(stream).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        } ?: ExifInterface.ORIENTATION_NORMAL

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.postRotate(90f)
                matrix.preScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.postRotate(270f)
                matrix.preScale(-1f, 1f)
            }
            else -> return bitmap
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun resizeToMaxDimension(source: Bitmap, maxDimension: Int): Bitmap {
        val longest = maxOf(source.width, source.height)
        if (longest <= maxDimension) return source

        val scale = maxDimension.toFloat() / longest
        val newWidth = (source.width * scale).roundToInt().coerceAtLeast(1)
        val newHeight = (source.height * scale).roundToInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
    }
}
