package com.amefure.capsuletoyapp.Repository.Interface

import android.graphics.Bitmap


interface ImageRepository {
    fun saveBitmapToInternalStorage(bitmap: Bitmap?, fileName: String): String?
    fun fetchImage(filePath: String?): Bitmap?
}
