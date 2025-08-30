package com.amefure.capsuletoyapp.Models.Domain.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<Category>): List<Long>

    /** Upsert == あればUpdateなければInsert */
    @Upsert
    suspend fun upsertCategories(categories: List<Category>): List<Long>

    @Query("DELETE FROM ${Category.TABLE_NAME} WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)

}