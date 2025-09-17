package com.amefure.capsuletoyapp.models.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.models.domain.entity.Location

@Dao
interface LocationDao {

    /** 対象シリーズIDのLocationを全て取得 */
    @Query("SELECT * FROM ${Location.TABLE_NAME} WHERE seriesId = :seriesId")
    suspend fun fetchLocations(seriesId: Long): List<Location>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<Location>): List<Long>

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertLocations(locations: List<Location>): List<Long>

    @Query("DELETE FROM ${Location.TABLE_NAME} WHERE id = :locationId")
    suspend fun deleteLocationById(locationId: Long)
}
