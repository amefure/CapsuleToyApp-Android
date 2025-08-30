package com.amefure.capsuletoyapp.Repository

import android.content.Context
import com.amefure.capsuletoyapp.Models.Domain.Dao.CapsuleToyDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.CategoryDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.LocationDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.SeriesCategoryCrossRefDao
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

    private val database = AppDatabase.getDatabase(context)

    /** Dao */
    private val seriesDao: SeriesDao = database.seriesDao()
    private val capsuleToyDao: CapsuleToyDao = database.capsuleToyDao()
    private val locationDao: LocationDao = database.locationDao()
    private val categoryDao: CategoryDao = database.categoryDao()
    private val seriesCategoryCrossRefDao: SeriesCategoryCrossRefDao = database.seriesCategoryCrossRefDao()

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
        if (capsuleToys.isNotEmpty()) {
            capsuleToyDao.insertCapsuleToys(capsuleToys.map { it.copy(seriesId = seriesId) })
        }

        if (locations.isNotEmpty()) {
            locationDao.insertLocations(locations.map { it.copy(seriesId = seriesId) })
        }

        if (categories.isNotEmpty()) {
            val categoryIds = categoryDao.insertCategories(categories)
            val crossRefs = categoryIds.map { SeriesCategoryCrossRef(seriesId, it) }
            seriesCategoryCrossRefDao.insertSeriesCategoryCrossRef(crossRefs)
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
            capsuleToyDao.upsertCapsuleToys(capsuleToys)
        }

        if (locations.isNotEmpty()) {
            locationDao.upsertLocations(locations)
        }
        if (categories.isNotEmpty()) {
            // Category をUpsertして id を取得
            val categoryIds = categoryDao.upsertCategories(categories)
            // CrossRef をUpsertして id を取得
            val crossRefs = categoryIds.map { SeriesCategoryCrossRef(series.id, it) }
            seriesCategoryCrossRefDao.upsertSeriesCategoryCrossRefs(crossRefs)


            val existingCrossRefs = seriesCategoryCrossRefDao.getCrossRefsForSeries(series.id)
            val existingCategoryIds = existingCrossRefs.map { it.categoryId }
        }
    }

    override suspend fun deleteSeries(series: Series) {
        seriesDao.deleteSeriesById(series.id)
    }
}
