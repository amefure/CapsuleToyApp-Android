package com.amefure.capsuletoyapp.models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amefure.capsuletoyapp.models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.models.Domain.Entity.Series


@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSeries(series: Series): Long

    @Update
    suspend fun updateSeries(series: Series)

    @Query("UPDATE ${Series.TABLE_NAME} SET imagePath = :imagePath WHERE id = :id")
    suspend fun updateImagePath(id: Long, imagePath: String)

    @Query("DELETE FROM ${Series.TABLE_NAME}")
    fun deleteAll()

    @Query("DELETE FROM ${Series.TABLE_NAME} WHERE id = :seriesId")
    suspend fun deleteSeriesById(seriesId: Long)

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE id = :seriesId")
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME}")
    suspend fun fetchAll(): List<SeriesWithRelations>
}

