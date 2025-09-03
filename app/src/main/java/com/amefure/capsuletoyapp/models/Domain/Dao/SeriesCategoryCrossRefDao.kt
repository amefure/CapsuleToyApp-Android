package com.amefure.capsuletoyapp.models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.models.Domain.Entity.Relation.SeriesCategoryCrossRef

@Dao
interface SeriesCategoryCrossRefDao {

    @Query("SELECT * FROM ${SeriesCategoryCrossRef.TABLE_NAME} WHERE seriesId = :seriesId")
    suspend fun getCrossRefsForSeries(seriesId: Long): List<SeriesCategoryCrossRef>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSeriesCategoryCrossRef(crossRefs: List<SeriesCategoryCrossRef>): List<Long>

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertSeriesCategoryCrossRefs(crossRefs: List<SeriesCategoryCrossRef>): List<Long>

    @Query("SELECT COUNT(*) FROM ${SeriesCategoryCrossRef.TABLE_NAME} WHERE categoryId = :categoryId")
    suspend fun countCrossRefsForCategory(categoryId: Long): Int

    @Query("DELETE FROM ${SeriesCategoryCrossRef.TABLE_NAME} WHERE seriesId = :seriesId AND categoryId IN (:categoryIds)")
    suspend fun deleteSeriesCategoryCrossRefs(seriesId: Long, categoryIds: List<Long>)

}