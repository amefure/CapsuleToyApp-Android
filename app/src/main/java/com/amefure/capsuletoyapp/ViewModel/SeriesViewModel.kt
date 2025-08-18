package com.amefure.capsuletoyapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val repository: SeriesRepository
) : ViewModel() {

    private val _series = MutableLiveData<List<SeriesWithRelations>>()
    val series: LiveData<List<SeriesWithRelations>?> = _series

    fun fetchSingleSeries(seriesId: Long) {
        viewModelScope.launch {
            val data = repository.fetchSingleSeries(seriesId)
            _series.postValue(listOf(data))
        }
    }

    fun fetchAllSeries() {
        viewModelScope.launch {
            Log.d("VM", "読み込み")
            val data = repository.fetchAllSeries()
            _series.postValue(data)
        }
    }

    fun addSeries(
        name: String,
        count: Int,
        amount: Int,
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
