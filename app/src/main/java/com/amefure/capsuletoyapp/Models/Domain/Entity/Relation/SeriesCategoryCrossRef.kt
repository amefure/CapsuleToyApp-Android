package com.amefure.capsuletoyapp.Models.Domain.Entity.Relation

import androidx.room.Entity

/** Series と Category を多対多にするためにIDを管理するクラス */
@Entity(
    tableName = "series_category_cross_ref",
    primaryKeys = ["seriesId", "categoryId"]
)
data class SeriesCategoryCrossRef(
    val seriesId: Long,
    val categoryId: Long
)