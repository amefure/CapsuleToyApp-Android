package com.amefure.capsuletoyapp.ViewModel

import android.util.Log
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
    public var categories: MutableList<Category> = mutableListOf()
        private set

    public var showSuccessDialog by mutableStateOf(false)
        private set
    public var showValidationDialog by mutableStateOf(false)
        private set

    /** シリーズIDで指定されたシリーズ情報を取得してUIに反映する */
    public fun fetchSingleSeries(seriesId: Long) {
        // IDが0Lなら取得しない
        if (seriesId == 0L) return
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
        categories = entity.categories.toMutableList()
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
        seriesId: Long,
        capsuleToys: List<CapsuleToy> = emptyList(),
        locations: List<Location> = emptyList(),
    ) {
        if (name.isEmpty() || count == null|| count == 0) {
            showValidationAlert()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            series?.series?.let { series ->
                // 更新
                series.name = name
                series.count = count ?: 1
                series.amount = amount
                series.memo = memo
                series.imagePath = null
                Log.d("DDDUPDATEA", categories.size.toString())
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
                Log.d("DDDINSERT", categories.size.toString())
                repository.insertSeries(series, capsuleToys, locations, categories)
            }
            withContext(Dispatchers.Main) {
                showSuccessAlert()
            }
        }
    }

    public fun addCategory(
        category: Category
    ) {
        categories.add(category)
    }
}


