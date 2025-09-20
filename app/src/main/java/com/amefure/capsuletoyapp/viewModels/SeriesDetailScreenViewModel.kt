package com.amefure.capsuletoyapp.viewModels

import android.graphics.Bitmap
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesWithRelations
import com.amefure.capsuletoyapp.repositories.interfaces.ImageRepository
import com.amefure.capsuletoyapp.repositories.interfaces.SeriesRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class SeriesDetailScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
) : ViewModel() {

    /**
     *  ComposeなのでStateFlowではなくStateで保持する
     *  これによりView側で変換を検知したタイミングでRecompositionされる
     *  setのみprivateとする
     *  by(プロパティデリゲート)を使用することで.valueを省略
     */
    public var series: SeriesWithRelations? by mutableStateOf(null)
        private set

    public var showSuccessDialog by mutableStateOf(false)
        private set
    public var showConfirmDialog by mutableStateOf(false)
        private set

    /** 初期位置：スカイツリー：LatLng(35.710063, 139.8107) */
    public var initialLatLng: LatLng by mutableStateOf(LatLng(35.710063, 139.8107))
        private set

    public fun fetchImage(photoPath: String?): Bitmap? =
        imageFileRepository.fetchImage(photoPath)

    /** 総数カウント */
    public fun fetchTotalCount(): Int {
        // ユーザー登録カウント
        val userRegisterCount = series?.series?.count ?: 0
        // 登録Toyカウント
        val toysCount = series?.capsuleToys?.size ?: 0
        return max(userRegisterCount, toysCount)
    }

    /** 所持数カウント */
    public fun fetchIsOwnedCount(): Int {
        return series?.capsuleToys?.filter { it.isOwned }?.size ?: 0
    }

    public fun fetchSingleSeries(seriesId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchSingleSeries(seriesId)
            series = data
        }
    }

    public fun deleteSeries() {
        val series = series ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSeries(series.series)
        }
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
