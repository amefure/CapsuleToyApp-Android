package com.amefure.capsuletoyapp.models.Domain.Entity.Relation

import androidx.room.Entity
import com.amefure.capsuletoyapp.models.Domain.Entity.Category
import com.amefure.capsuletoyapp.models.Domain.Entity.Series

/** Series と Category を多対多にするためにIDを管理するクラス */
@Entity(
    tableName = SeriesCategoryCrossRef.TABLE_NAME,
    primaryKeys = [Series.ID_KEY, Category.ID_KEY]
)
data class SeriesCategoryCrossRef(
    val seriesId: Long,
    val categoryId: Long
) {
    companion object {
        public const val TABLE_NAME = "series_category_cross_ref"
    }
}