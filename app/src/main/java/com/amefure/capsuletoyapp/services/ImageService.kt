package com.amefure.capsuletoyapp.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ImageService(private val context: Context) {

    fun decodeUriToBitmap(uri: Uri): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source)
    }

    fun createTempPhotoUri(): Uri {
        val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile,
        )
    }
}