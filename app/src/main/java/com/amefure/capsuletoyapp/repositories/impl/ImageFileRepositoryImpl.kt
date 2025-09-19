package com.amefure.capsuletoyapp.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.amefure.capsuletoyapp.repositories.interfaces.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageFileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageRepository {

    override fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String): String? {
        val bitmap = bitmap ?: return null
        // 内部ストレージに保存
        val file = File(context.filesDir, "$fileName.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.absolutePath
    }

    override fun fetchImage(filePath: String?): Bitmap? {
        val filePath = filePath ?: return null
        val file = File(filePath)
        return if (file.exists()) BitmapFactory.decodeFile(filePath) else null
    }
}
