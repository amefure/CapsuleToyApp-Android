package com.amefure.capsuletoyapp.repositories.repositoryInterface

import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.domain.entity.Series
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesWithRelations

/**
 * アプリ内で扱うRepositoryインターフェース
 * 具体クラス：[RoomSeriesRepositoryImpl]
 */
interface SeriesRepository {
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    suspend fun fetchAllSeries(): List<SeriesWithRelations>

    /**
     * [capsuleToys]はシリーズのインサートでは登録されないため対象外
     */
    suspend fun insertSeries(
        series: Series,
        locations: List<Location>,
        categories: List<Category>,
    ): Long

    /**
     * [capsuleToys]はシリーズのアップデートでは更新されないため対象外
     */
    suspend fun updateSeries(
        series: Series,
        locations: List<Location>,
        categories: List<Category>,
    )

    suspend fun updateImagePathSeries(
        seriesId: Long,
        imagePath: String,
    )

    suspend fun deleteSeries(series: Series)
}
