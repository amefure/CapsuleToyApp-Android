package com.amefure.capsuletoyapp.repositories.impl

import android.content.Context
import com.amefure.capsuletoyapp.models.domain.dao.CapsuleToyDao
import com.amefure.capsuletoyapp.models.domain.dao.CategoryDao
import com.amefure.capsuletoyapp.models.domain.dao.LocationDao
import com.amefure.capsuletoyapp.models.domain.dao.SeriesCategoryCrossRefDao
import com.amefure.capsuletoyapp.models.domain.dao.SeriesDao
import com.amefure.capsuletoyapp.models.domain.database.AppDatabase
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.domain.entity.Series
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesCategoryCrossRef
import com.amefure.capsuletoyapp.models.domain.entity.relation.SeriesWithRelations
import com.amefure.capsuletoyapp.repositories.repositoryInterface.SeriesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/** Room(ローカル)エンティティ管理リポジトリ */
class RoomSeriesRepositoryImpl
@Inject constructor(
    @ApplicationContext private val context: Context,
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
        categories: List<Category>,
    ): Long {
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
        return seriesId
    }

    override suspend fun updateSeries(
        series: Series,
        capsuleToys: List<CapsuleToy>,
        locations: List<Location>,
        categories: List<Category>,
    ) {
        seriesDao.updateSeries(series)

        if (capsuleToys.isNotEmpty()) {
            capsuleToyDao.upsertCapsuleToys(capsuleToys)
        }

        if (locations.isNotEmpty()) {
            locationDao.upsertLocations(locations)
        }
        if (categories.isNotEmpty()) {
            // Upsertで更新 or 新規追加を行い新規追加したIDを取得する
            // 更新されたものは-1が返るため除外する
            val insertIds = categoryDao.upsertCategories(categories).filter { it != -1L }
            // 更新対象だったIDを取得(新規追加はIDが0になっている)
            val updateIds = categories.map { it.id }.filter { it != 0L }
            // 追加・更新したIDリスト
            val categoryIds = (insertIds + updateIds).toSet()
            // CrossRef をUpsertして id を取得
            val crossRefs = categoryIds.map { SeriesCategoryCrossRef(series.id, it) }
            seriesCategoryCrossRefDao.upsertSeriesCategoryCrossRefs(crossRefs)

            // すでに登録済みの対象のseriesIdのSeriesCategoryCrossRefを取得
            val existingCategoryIds = seriesCategoryCrossRefDao.getCrossRefsForSeries(series.id).map { it.categoryId }
            // 今回削除対象となる CategoryId
            val toDeleteCategoryIds = existingCategoryIds - categoryIds
            seriesCategoryCrossRefDao.deleteSeriesCategoryCrossRefs(series.id, toDeleteCategoryIds.toList())
            toDeleteCategoryIds.forEach { categoryId ->
                // 対象のカテゴリIDの参照がすでになければカテゴリ自体を削除する
                val refCount = seriesCategoryCrossRefDao.countCrossRefsForCategory(categoryId)
                if (refCount == 0) {
                    categoryDao.deleteCategoryById(categoryId)
                }
            }
        }
    }

    override suspend fun updateImagePathSeries(
        seriesId: Long,
        imagePath: String,
    ) {
        seriesDao.updateImagePath(seriesId, imagePath)
    }

    override suspend fun deleteSeries(series: Series) {
        seriesDao.deleteSeriesById(series.id)
    }
}
