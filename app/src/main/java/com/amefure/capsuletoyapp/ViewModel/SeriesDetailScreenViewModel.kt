package com.amefure.capsuletoyapp.ViewModel

import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Repository.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailScreenViewModel @Inject constructor(
    private val repository: SeriesRepository
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


    public fun fetchSingleSeries(seriesId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchSingleSeries(seriesId)
            series = data
        }
    }

    public fun deleteSeries() {
        val series = series?: return
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


