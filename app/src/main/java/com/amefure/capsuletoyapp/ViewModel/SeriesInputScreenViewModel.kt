package com.amefure.capsuletoyapp.ViewModel

import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Repository.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SeriesInputScreenViewModel @Inject constructor(
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

    /** UI連動プロパティ */
    public var name by mutableStateOf("")
    public var count: Int? by mutableStateOf(null)
    public var amount: Int? by mutableStateOf(null)
    public var memo by mutableStateOf("")

    public var showSuccessDialog by mutableStateOf(false)
        private set
    public var showValidationDialog by mutableStateOf(false)
        private set

    /** シリーズIDで指定されたシリーズ情報を取得してUIに反映する */
    public fun fetchSingleSeries(seriesId: Long) {
        // IDが0Lなら取得しない
        if (seriesId == 0L) return
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchSingleSeries(seriesId)
            series = data
            withContext(Dispatchers.Main) {
                applySeries(data.series)
            }
        }
    }

    /** シリーズ情報をUIに反映する */
    @MainThread
    private fun applySeries(series: Series) {
        name = series.name
        count = series.count
        amount = series.amount
        memo = series.memo
    }

    @MainThread
    public fun showSuccessAlert() {
        showSuccessDialog = true
    }
    @MainThread
    public fun showValidationAlert() {
        showValidationDialog = true
    }
    @MainThread
    public fun closeSuccessAlert() {
        showSuccessDialog = false
    }
    @MainThread
    public fun closeValidationAlert() {
        showValidationDialog = false
    }

    public fun createOrUpdateSeries(
        seriesId: Long,
        capsuleToys: List<CapsuleToy> = emptyList(),
        locations: List<Location> = emptyList(),
        categories: List<Category> = emptyList()
    ) {
        if (name.isEmpty() || count == null|| count == 0) {
            showValidationAlert()
            return
        }
        val series = Series(
            name = name,
            count = count ?: 1,
            amount = amount,
            memo = memo,
            imagePath = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            if (seriesId == 0L) {
                repository.insertSeries(series, capsuleToys, locations, categories)
            } else {
                repository.insertSeries(series, capsuleToys, locations, categories)
            }
            withContext(Dispatchers.Main) {
                showSuccessAlert()
            }
        }
    }
}


