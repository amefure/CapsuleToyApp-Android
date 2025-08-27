package com.amefure.capsuletoyapp.Models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesCategoryCrossRef
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesWithRelations
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series


@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSeries(series: Series): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCapsuleToys(toys: List<CapsuleToy>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<Location>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<Category>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSeriesCategoryCrossRef(crossRefs: List<SeriesCategoryCrossRef>)

    @Update
    suspend fun updateSeries(series: Series)

    @Update
    suspend fun updateCapsuleToy(toy: CapsuleToy)

    @Update
    suspend fun updateLocation(location: Location)

    @Update
    suspend fun updateCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE id = :seriesId")
    suspend fun fetchSingleSeries(seriesId: Long): SeriesWithRelations

    @Transaction
    @Query("SELECT * FROM ${Series.TABLE_NAME}")
    suspend fun fetchAll(): List<SeriesWithRelations>

    @Query("DELETE FROM ${Series.TABLE_NAME}")
    fun deleteAll()

    @Query("DELETE FROM ${Series.TABLE_NAME} WHERE id = :seriesId")
    fun deleteSeries(seriesId: Long)

}

