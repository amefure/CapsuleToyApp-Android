package com.amefure.capsuletoyapp.repositories.repositoryInterface

import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.domain.entity.Series
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesWithRelations

/** アプリ内で扱うRepositoryインターフェース */
interface SeriesRepository {
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    suspend fun fetchAllSeries(): List<SeriesWithRelations>

    suspend fun insertSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>,
    ): Long

    suspend fun updateSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>,
    )

    suspend fun updateImagePathSeries(
        seriesId: Long,
        imagePath: String,
    )

    suspend fun deleteSeries(series: Series)
}
