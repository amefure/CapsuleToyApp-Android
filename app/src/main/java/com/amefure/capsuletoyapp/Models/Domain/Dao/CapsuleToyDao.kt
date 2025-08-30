package com.amefure.capsuletoyapp.Models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy

@Dao
interface CapsuleToyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCapsuleToys(capsuleToys: List< CapsuleToy>): List<Long>

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertCapsuleToys(capsuleToys: List<CapsuleToy>): List<Long>

    @Query("DELETE FROM ${CapsuleToy.TABLE_NAME} WHERE id = :capsuleToyId")
    suspend fun deleteCapsuleToyById(capsuleToyId: Long)

}