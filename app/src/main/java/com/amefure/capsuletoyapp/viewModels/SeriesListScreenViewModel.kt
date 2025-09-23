package com.amefure.capsuletoyapp.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesMockFactory
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesWithRelations
import com.amefure.capsuletoyapp.repositories.interfaces.ImageRepository
import com.amefure.capsuletoyapp.repositories.interfaces.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class SeriesListScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository,
) : ViewModel() {

    /**
     *  ComposeなのでStateFlowではなくStateで保持する
     *  これによりView側で変換を検知したタイミングでRecompositionされる
     *  setのみprivateとする
     *  by(プロパティデリゲート)を使用することで.valueを省略
     */
    public var series: List<SeriesWithRelations> by mutableStateOf(emptyList())
        private set

    public fun fetchAllSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchAllSeries()
            series = data
        }
    }

    private fun createMockData() {
        val seriesList = SeriesMockFactory.mockList()
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchAllSeries()
            // すでにデータが存在するなら再度モックを登録しない
            if (data.isNotEmpty()) return@launch
            seriesList.forEach {
                val seriesId = repository.insertSeries(it.series, it.locations, it.categories)
                it.capsuleToys.forEach {
                    it.seriesId = seriesId
                    repository.insertCapsuleToy(it)
                }
            }
        }
    }

    public fun fetchImage(imagePath: String?): Bitmap? =
        imageFileRepository.fetchImage(imagePath)

    /** 総数カウント */
    public fun fetchTotalCount(series: SeriesWithRelations): Int {
        // ユーザー登録カウント
        val userRegisterCount = series.series.count
        // 登録Toyカウント
        val toysCount = series.capsuleToys.size
        return max(userRegisterCount, toysCount)
    }

    /** 所持数カウント */
    public fun fetchIsOwnedCount(series: SeriesWithRelations): Int {
        return series.capsuleToys.filter { it.isOwned }.size
    }
}
