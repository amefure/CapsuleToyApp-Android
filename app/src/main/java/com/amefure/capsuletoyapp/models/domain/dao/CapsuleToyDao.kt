package com.amefure.capsuletoyapp.models.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy

@Dao
interface CapsuleToyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCapsuleToy(capsuleToy: CapsuleToy): Long

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertCapsuleToys(capsuleToys: List<CapsuleToy>): List<Long>

    @Query("UPDATE ${CapsuleToy.TABLE_NAME} SET imagePath = :imagePath WHERE id = :id")
    suspend fun updateImagePath(capsuleToyId: Long, imagePath: String)

    @Query("DELETE FROM ${CapsuleToy.TABLE_NAME} WHERE id = :capsuleToyId")
    suspend fun deleteCapsuleToyById(capsuleToyId: Long)
}
