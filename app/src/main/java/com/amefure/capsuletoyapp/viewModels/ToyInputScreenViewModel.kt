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
import com.amefure.capsuletoyapp.repositories.interfaces.ImageRepository
import com.amefure.capsuletoyapp.repositories.interfaces.SeriesRepository
import com.amefure.capsuletoyapp.services.ImageService
import com.amefure.capsuletoyapp.viewModels.interfaces.CameraInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ToyInputScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
    @ApplicationContext private val context: Context,
) : ViewModel(), CameraInterface {

    public var toy: CapsuleToy? by mutableStateOf(null)
        private set

    /** UI連動プロパティ */
    public var name by mutableStateOf("")
    public var memo by mutableStateOf("")
    public var isOwned by mutableStateOf(false)
    public var isSecret by mutableStateOf(false)
    public var isGetDate: Long? by mutableStateOf(null)

    /** ドロップダウンメニューの開閉フラグ */
    override var expanded by mutableStateOf(false)

    /** カメラで撮影した画像を保持 */
    override var thumbnail: Bitmap? by mutableStateOf(null)

    /** 一時保存用のURI */
    override var photoUri: Uri? = null
        private set

    public var isShowSuccessDialog by mutableStateOf(false)
        private set

    public var isShowValidationDialog by mutableStateOf(false)
        private set

    private val imageService = ImageService(context)

    /** 一時保存用のURIを構築 */
    override fun preparePhotoUri() {
        photoUri = imageService.createTempPhotoUri()
    }

    /** カメラで撮影した画像を格納 */
    override fun onCameraCaptured() {
        val photoUri = photoUri ?: return
        thumbnail = imageService.decodeUriToBitmap(photoUri)
    }

    /** ギャラリーから選択された画像を格納 */
    override fun onGalleryImageSelected(uri: Uri) {
        thumbnail = imageService.decodeUriToBitmap(uri)
    }

    public fun fetchSingleToy(toyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = repository.fetchSingleToy(toyId) ?: return@launch
            toy = entity
            withContext(Dispatchers.Main) {
                applySeries(entity)
            }
        }
    }

    /** Toy情報をUIに反映する */
    @MainThread
    private fun applySeries(entity: CapsuleToy) {
        name = entity.name
        isOwned = entity.isOwned
        isSecret = entity.isSecret
        isGetDate = entity.isGetAt?.time
        memo = entity.memo
        thumbnail = imageFileRepository.fetchImage(entity.imagePath)
    }

    public fun convertDate(epochMilli: Long?): LocalDate? {
        val epochMilli = epochMilli ?: return null
        return Instant.ofEpochMilli(epochMilli)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    public fun createOrUpdateCapsuleToy(
        seriesId: Long,
    ) {
        if (name.isEmpty()) {
            showValidationAlert()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            toy?.let { toy ->
                // 更新
                val fileName = "$seriesId-${toy.id}"
                val imagePath = imageFileRepository.saveBitmapToInternalStorage(thumbnail, fileName)
                toy.name = name
                toy.isOwned = isOwned
                toy.isSecret = isSecret
                toy.memo = memo
                toy.imagePath = imagePath
                toy.isGetAt = isGetDate?.let { Date(it) }
                repository.updateCapsuleToy(toy)
            } ?: run {
                // 新規登録
                val capsuleToy = CapsuleToy(
                    seriesId = seriesId,
                    name = name,
                    isOwned = isOwned,
                    isSecret = isSecret,
                    memo = memo,
                    imagePath = null,
                    isGetAt = isGetDate?.let { Date(it) },
                )
                val capsuleToyId = repository.insertCapsuleToy(capsuleToy)
                val fileName = "$seriesId-$capsuleToyId"
                val imagePath = imageFileRepository.saveBitmapToInternalStorage(thumbnail, fileName)
                imagePath?.let {
                    capsuleToy.imagePath = imagePath
                    repository.updateImagePathCapsuleToy(capsuleToyId, imagePath)
                }
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
