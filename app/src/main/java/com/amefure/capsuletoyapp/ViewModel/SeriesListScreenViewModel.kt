package com.amefure.capsuletoyapp.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Repository.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SeriesListScreenViewModel @Inject constructor(
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

    public fun fetchAllSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("VM", "読み込み")
            val data = repository.fetchAllSeries()
            series = data
        }
    }
}


