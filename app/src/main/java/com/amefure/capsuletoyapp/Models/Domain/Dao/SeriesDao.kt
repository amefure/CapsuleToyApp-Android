package com.amefure.capsuletoyapp.Models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Models.Domain.Entity.SeriesCategoryCrossRef
import com.amefure.capsuletoyapp.Models.Domain.Entity.SeriesWithRelations


@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: Series): Long

    @Insert
    suspend fun insertCapsuleToys(toys: List<CapsuleToy>)

    @Insert
    suspend fun insertLocations(locations: List<Location>)

    @Insert
    suspend fun insertCategories(categories: List<Category>): List<Long>

    @Insert
    suspend fun insertSeriesCategoryCrossRef(crossRefs: List<SeriesCategoryCrossRef>)

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE id = :seriesId")
    suspend fun getSeriesWithRelations(seriesId: Long): SeriesWithRelations

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME}")
    suspend fun fetchAll(): List<SeriesWithRelations>

    @Query("DELETE FROM ${Series.TABLE_NAME}")
    fun deleteAll()

}

