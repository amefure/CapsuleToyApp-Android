package com.amefure.capsuletoyapp.viewModels

import android.graphics.Bitmap
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.repositories.interfaces.ImageRepository
import com.amefure.capsuletoyapp.repositories.interfaces.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ToyDetailScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
) : ViewModel() {

    public var toy: CapsuleToy? by mutableStateOf(null)
        private set

    public var showSuccessDialog by mutableStateOf(false)
        private set
    public var showConfirmDialog by mutableStateOf(false)
        private set

    public fun fetchImage(photoPath: String?): Bitmap? =
        imageFileRepository.fetchImage(photoPath)

    public fun fetchSingleToy(toyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = repository.fetchSingleToy(toyId) ?: return@launch
            toy = entity
        }
    }

    public fun deleteToy() {
        val toy = toy ?: return
        viewModelScope.launch(Dispatchers.IO) {
            // カプセルトイのサムネイル画像があれば削除
            imageFileRepository.deleteBitmapFromInternalStorage(toy.imagePath)
            repository.deleteCapsuleToy(toy)
        }
    }

    public fun convertDate(epochMilli: Long?): LocalDate? {
        val epochMilli = epochMilli ?: return null
        return Instant.ofEpochMilli(epochMilli)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @MainThread
    public fun showSuccessAlert() {
        showSuccessDialog = true
    }

    @MainThread
    public fun showConfirmAlert() {
        showConfirmDialog = true
    }

    @MainThread
    public fun closeSuccessAlert() {
        showSuccessDialog = false
    }

    @MainThread
    public fun closeConfirmAlert() {
        showConfirmDialog = false
    }
}
