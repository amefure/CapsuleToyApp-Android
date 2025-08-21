package com.amefure.capsuletoyapp.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Repository.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val repository: SeriesRepository
) : ViewModel() {

    /**
     *  ComposeなのでStateFlowではなくStateで保持する
     *  これによりView側で変換を検知したタイミングでRecompositionされる
     *  setのみprivateとする
     *  by(プロパティデリゲート)を使用することで.valueを省略
     */
    public var series: List<SeriesWithRelations> by mutableStateOf(emptyList())
        private set

    fun fetchSingleSeries(seriesId: Long) {
        viewModelScope.launch {
            val data = repository.fetchSingleSeries(seriesId)
            series = listOf(data)
        }

    }

    fun fetchAllSeries() {
        viewModelScope.launch {
            Log.d("VM", "読み込み")
            val data = repository.fetchAllSeries()
            series = data
        }
    }

    fun addSeries(
        name: String,
        count: Int,
        amount: Int?,
        memo: String,
        capsuleToys: List<CapsuleToy> = emptyList(),
        locations: List<Location> = emptyList(),
        categories: List<Category> = emptyList()
    ) {
        val series = Series(
            name = name,
            count = count,
            amount = amount,
            memo = memo,
            imagePath = null
        )
        viewModelScope.launch {
            repository.insertSeries(series, capsuleToys, locations, categories)
        }
    }

    fun deleteSeries(series: Series) {
        viewModelScope.launch {
            repository.deleteSeries(series)
        }
    }
}


