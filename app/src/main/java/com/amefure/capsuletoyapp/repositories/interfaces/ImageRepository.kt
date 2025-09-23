package com.amefure.capsuletoyapp.repositories.interfaces

import android.graphics.Bitmap

interface ImageRepository {
    fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String): String?
    fun fetchImage(filePath: String?): Bitmap?
    fun deleteBitmapFromInternalStorage(filePath: String?): Boolean
}
