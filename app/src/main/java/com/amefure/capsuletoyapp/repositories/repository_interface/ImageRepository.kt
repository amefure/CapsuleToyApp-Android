package com.amefure.capsuletoyapp.repositories.repository_interface

import android.graphics.Bitmap


interface ImageRepository {
    fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String): String?
    fun fetchImage(filePath: String?): Bitmap?
}
