package com.amefure.capsuletoyapp.ViewModel

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

    public fun fetchSingleSeries(seriesId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchSingleSeries(seriesId)
            series = data
        }
    }

    public fun addSeries(
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSeries(series, capsuleToys, locations, categories)
        }
    }

    public fun updateSeries(
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSeries(series, capsuleToys, locations, categories)
        }
    }
}


