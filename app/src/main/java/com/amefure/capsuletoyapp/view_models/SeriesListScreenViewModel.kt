package com.amefure.capsuletoyapp.view_models

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.repositories.repository_interface.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.amefure.capsuletoyapp.repositories.repository_interface.ImageRepository
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SeriesListScreenViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val imageFileRepository: ImageRepository
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

    public fun fetchImage(imagePath: String?): Bitmap? =
        imageFileRepository.fetchImage(imagePath)
}


