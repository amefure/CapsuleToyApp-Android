package com.amefure.capsuletoyapp.Repository

import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series

/** アプリ内で扱うRepositoryインターフェース */
interface SeriesRepository {
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    suspend fun fetchAllSeries(): List<SeriesWithRelations>

    suspend fun insertSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>
    )
    suspend fun deleteSeries(series: Series)
}