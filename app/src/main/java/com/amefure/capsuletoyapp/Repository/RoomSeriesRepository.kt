package com.amefure.capsuletoyapp.Repository

import android.content.Context
import android.util.Log
import com.amefure.capsuletoyapp.Models.Domain.Dao.SeriesDao
import com.amefure.capsuletoyapp.Models.Domain.Database.AppDatabase
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesCategoryCrossRef
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/** Room(ローカル)エンティティ管理リポジトリ */
class RoomSeriesRepositoryImpl
@Inject constructor(
    @ApplicationContext private val context: Context
) : SeriesRepository {
    /** Dao */
    private val seriesDao: SeriesDao = AppDatabase.getDatabase(context).seriesDao()

    override suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations =
        seriesDao.fetchSingleSeries(seriesId)

    override suspend fun fetchAllSeries(): List<SeriesWithRelations> =
        seriesDao.fetchAll()

    override suspend fun insertSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>
    ) {
        val seriesId = seriesDao.insertSeries(series)
        Log.d("シリーズID", seriesId.toString())
        if (capsuleToys.isNotEmpty()) {
            seriesDao.insertCapsuleToys(capsuleToys.map { it.copy(seriesId = seriesId) })
        }

        if (locations.isNotEmpty()) {
            seriesDao.insertLocations(locations.map { it.copy(seriesId = seriesId) })
        }

        if (categories.isNotEmpty()) {
            // Category を保存して id を取得
            val categoryIds = seriesDao.insertCategories(categories)
            Log.d("カテゴリID", categoryIds.toString())
            // CrossRef を保存
            val crossRefs = categoryIds.map { SeriesCategoryCrossRef(seriesId, it) }
            seriesDao.insertSeriesCategoryCrossRef(crossRefs)
        }
    }

    override suspend fun updateSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>
    ) {
        seriesDao.updateSeries(series)

        if (capsuleToys.isNotEmpty()) {
            capsuleToys.forEach {
                seriesDao.updateCapsuleToy(it)
            }
        }

        if (locations.isNotEmpty()) {
            locations.forEach {
                seriesDao.updateLocation(it)
            }
        }
        if (categories.isNotEmpty()) {
            categories.forEach {
                seriesDao.updateCategory(it)
            }
        }
    }

    override suspend fun deleteSeries(series: Series) {
        seriesDao.deleteSeries(series.id)
    }
}
