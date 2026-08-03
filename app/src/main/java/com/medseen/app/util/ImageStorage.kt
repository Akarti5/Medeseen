package com.medseen.app.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * Copies a picked gallery image into app-private storage so it remains
 * available after the picker URI expires.
 */
object ImageStorage {

    fun savePickedImage(context: Context, uri: Uri): String {
        val photosDir = File(context.filesDir, "photos").apply { mkdirs() }
        val dest = File(photosDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Unable to read selected image")
        return dest.absolutePath
    }
}
