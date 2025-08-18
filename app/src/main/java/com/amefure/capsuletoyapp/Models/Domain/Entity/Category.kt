package com.amefure.capsuletoyapp.Models.Domain.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** カテゴリ名 */
    val name: String,
    /** 色(16進数) */
    val colorHex: String
)