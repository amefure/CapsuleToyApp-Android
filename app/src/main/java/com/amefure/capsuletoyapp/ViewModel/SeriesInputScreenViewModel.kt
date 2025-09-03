package com.amefure.capsuletoyapp.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Repository.Interface.ImageRepository
import com.amefure.capsuletoyapp.Repository.Interface.SeriesRepository
import com.amefure.capsuletoyapp.services.ImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SeriesInputScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     *  ComposeなのでStateFlowではなくStateで保持する
     *  これによりView側で変換を検知したタイミングでRecompositionされる
     *  setのみprivateとする
     *  by(プロパティデリゲート)を使用することで.valueを省略
     */
    public var series: SeriesWithRelations? by mutableStateOf(null)
        private set

    /** UI連動プロパティ */
    public var name by mutableStateOf("")
    public var count: Int? by mutableStateOf(null)
    public var amount: Int? by mutableStateOf(null)
    public var memo by mutableStateOf("")

    /** カメラで撮影した画像を保持 */
    public var thumbnail: Bitmap? by mutableStateOf(null)

    // コレクション型はSnapshotStateListで管理しないと要素の変化では再コンポーズされない
    public var categories: SnapshotStateList<Category> = mutableStateListOf()
        private set

    public var showSuccessDialog by mutableStateOf(false)
        private set
    public var showValidationDialog by mutableStateOf(false)
        private set

    private val imageService = ImageService(context)

    /** シリーズIDで指定されたシリーズ情報を取得してUIに反映する */
    public fun fetchSingleSeries(seriesId: Long) {
        // IDが0Lなら取得しない
        if (seriesId == 0L || series != null) return
        viewModelScope.launch(Dispatchers.IO) {
            val entity = repository.fetchSingleSeries(seriesId)
            series = entity
            withContext(Dispatchers.Main) {
                applySeries(entity)
            }
        }
    }

    /** シリーズ情報をUIに反映する */
    @MainThread
    private fun applySeries(entity: SeriesWithRelations) {
        val series = entity.series
        name = series.name
        count = series.count
        amount = series.amount
        memo = series.memo
        thumbnail = imageFileRepository.fetchImage(series.imagePath)
        // categories = entity.categories.toMutableStateList()
        // SnapshotStateListなので上記では再Composeされないので明示的に空にして追加する
        categories.clear()
        categories.addAll(entity.categories)
    }

    @MainThread
    public fun showSuccessAlert() {
        showSuccessDialog = true
    }
    @MainThread
    public fun closeSuccessAlert() {
        showSuccessDialog = false
    }
    @MainThread
    public fun showValidationAlert() {
        showValidationDialog = true
    }
    @MainThread
    public fun closeValidationAlert() {
        showValidationDialog = false
    }

    /** SeriesInput画面から新規作成・更新する */
    public fun createOrUpdateSeries(
        capsuleToys: List<CapsuleToy> = emptyList(),
        locations: List<Location> = emptyList(),
    ) {
        if (name.isEmpty() || count == null|| count == 0) {
            showValidationAlert()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            series?.series?.let { series ->
                val imagePath = imageFileRepository.saveBitmapToInternalStorage(thumbnail, series.id.toString())
                // 更新
                series.name = name
                series.count = count ?: 1
                series.amount = amount
                series.memo = memo
                series.imagePath = imagePath
                repository.updateSeries(series, capsuleToys, locations, categories)
            } ?: run {
                // 新規追加
                val series = Series(
                    name = name,
                    count = count ?: 1,
                    amount = amount,
                    memo = memo,
                    imagePath = null
                )
                val seriesId = repository.insertSeries(series, capsuleToys, locations, categories)
                val imagePath = imageFileRepository.saveBitmapToInternalStorage(thumbnail, seriesId.toString())
                imagePath?.let {
                    series.imagePath = imagePath
                    repository.updateImagePathSeries(seriesId, imagePath)
                }
            }
            withContext(Dispatchers.Main) {
                showSuccessAlert()
            }
        }
    }

    public var photoUri: Uri? = null
        private set

    private fun preparePhotoUri() {
        photoUri = imageService.createTempPhotoUri()
    }

    /** カメラで撮影した画像を格納 */
    public fun onCameraCaptured() {
        preparePhotoUri()
        val photoUri = photoUri ?: return
        thumbnail = imageService.decodeUriToBitmap(photoUri)
    }

    public fun onGalleryImageSelected(uri: Uri) {
        thumbnail = imageService.decodeUriToBitmap(uri)
    }

    public fun addCategory(
        category: Category
    ) {
        categories.add(category)
    }

    public fun removeCategory(
        category: Category
    ) {
        categories.remove(category)
    }
}


