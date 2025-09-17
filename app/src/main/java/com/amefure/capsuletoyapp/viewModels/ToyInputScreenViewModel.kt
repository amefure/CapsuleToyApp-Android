package com.amefure.capsuletoyapp.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.repositories.repositoryInterface.ImageRepository
import com.amefure.capsuletoyapp.repositories.repositoryInterface.SeriesRepository
import com.amefure.capsuletoyapp.services.ImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ToyInputScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    /** UI連動プロパティ */
    public var name by mutableStateOf("")
    public var memo by mutableStateOf("")
    public var isOwned by mutableStateOf(false)
    public var isSecret by mutableStateOf(false)

    /** ドロップダウンメニューの開閉フラグ */
    public var expanded by mutableStateOf(false)

    /** カメラで撮影した画像を保持 */
    public var thumbnail: Bitmap? by mutableStateOf(null)

    public var isShowSuccessDialog by mutableStateOf(false)
        private set

    public var isShowValidationDialog by mutableStateOf(false)
        private set

    /** 一時保存用のURI */
    public var photoUri: Uri? = null
        private set

    private val imageService = ImageService(context)

    /** 一時保存用のURIを構築 */
    public fun preparePhotoUri() {
        photoUri = imageService.createTempPhotoUri()
    }

    /** カメラで撮影した画像を格納 */
    public fun onCameraCaptured() {
        val photoUri = photoUri ?: return
        thumbnail = imageService.decodeUriToBitmap(photoUri)
    }

    /** ギャラリーから選択された画像を格納 */
    public fun onGalleryImageSelected(uri: Uri) {
        thumbnail = imageService.decodeUriToBitmap(uri)
    }

    public fun createCapsuleToy(
        seriesId: Long,
    ) {
        if (name.isEmpty()) {
            showValidationAlert()
            return
        }

        val capsuleToy = CapsuleToy(
            seriesId = seriesId,
            name = name,
            isOwned = isOwned,
            isSecret = isSecret,
            memo = memo,
            imagePath = null,
            isGetAt = Date(),
        )

        viewModelScope.launch(Dispatchers.IO) {
            val capsuleToyId = repository.insertCapsuleToy(capsuleToy)
            val imagePath = imageFileRepository.saveBitmapToInternalStorage(thumbnail, seriesId.toString() + "-" + capsuleToyId.toString())
            imagePath?.let {
                capsuleToy.imagePath = imagePath
                repository.updateImagePathCapsuleToy(capsuleToyId, imagePath)
            }
            withContext(Dispatchers.Main) {
                showSuccessAlert()
            }
        }
    }

    @MainThread
    public fun showSuccessAlert() {
        isShowSuccessDialog = true
    }

    @MainThread
    public fun closeSuccessAlert() {
        isShowSuccessDialog = false
    }

    @MainThread
    public fun showValidationAlert() {
        isShowValidationDialog = true
    }

    @MainThread
    public fun closeValidationAlert() {
        isShowValidationDialog = false
    }
}
