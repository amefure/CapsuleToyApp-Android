package com.amefure.capsuletoyapp.repositories.repository_interface

import com.amefure.capsuletoyapp.models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.models.Domain.Entity.Category
import com.amefure.capsuletoyapp.models.Domain.Entity.Location
import com.amefure.capsuletoyapp.models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.models.Domain.Entity.Series

/** アプリ内で扱うRepositoryインターフェース */
interface SeriesRepository {
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    suspend fun fetchAllSeries(): List<SeriesWithRelations>

    suspend fun insertSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>
    ): Long

    suspend fun updateSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>
    )

    suspend fun updateImagePathSeries(
        seriesId: Long,
        imagePath: String
    )

    suspend fun deleteSeries(series: Series)
}