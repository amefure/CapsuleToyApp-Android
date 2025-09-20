package com.amefure.capsuletoyapp.models.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy

@Dao
interface CapsuleToyDao {

    @Transaction
    @Query("SELECT * FROM ${CapsuleToy.TABLE_NAME} WHERE id = :capsuleToyId")
    suspend fun fetchSingleCapsuleToy(capsuleToyId: Long): CapsuleToy?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCapsuleToy(capsuleToy: CapsuleToy): Long

    @Update
    suspend fun updateCapsuleToy(capsuleToy: CapsuleToy)

    @Query("UPDATE ${CapsuleToy.TABLE_NAME} SET imagePath = :imagePath WHERE id = :capsuleToyId")
    suspend fun updateImagePath(capsuleToyId: Long, imagePath: String)

    @Query("DELETE FROM ${CapsuleToy.TABLE_NAME} WHERE id = :capsuleToyId")
    suspend fun deleteCapsuleToyById(capsuleToyId: Long)
}
