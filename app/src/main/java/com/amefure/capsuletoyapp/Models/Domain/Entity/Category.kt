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


/// Series と Category の中間テーブル
@Entity(
    tableName = "series_category_cross_ref",
    primaryKeys = ["seriesId", "categoryId"]
)
data class SeriesCategoryCrossRef(
    val seriesId: Long,
    val categoryId: Long
)