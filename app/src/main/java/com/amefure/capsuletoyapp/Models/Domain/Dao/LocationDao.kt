package com.amefure.capsuletoyapp.Models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<Location>): List<Long>

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertLocations(locations: List<Location>): List<Long>

    @Query("DELETE FROM ${Location.TABLE_NAME} WHERE id = :locationId")
    suspend fun deleteLocationById(locationId: Long)
}