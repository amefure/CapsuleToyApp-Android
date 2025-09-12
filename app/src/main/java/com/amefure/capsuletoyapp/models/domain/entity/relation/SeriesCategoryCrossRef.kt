package com.amefure.capsuletoyapp.models.domain.entity.relation

import androidx.room.Entity
import androidx.room.Index
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Series

/** Series と Category を多対多にするためにIDを管理するクラス */
@Entity(
    tableName = SeriesCategoryCrossRef.TABLE_NAME,
    primaryKeys = [Series.ID_KEY, Category.ID_KEY],
    // 警告解消：The column categoryId in the junction entity ... is being used to resolve a relationship but it is not covered by any index.
    // 「リレーションを解決するときに全件スキャンになる可能性がある」と警告
    // 該当カラムに @Index を付けてインデックスを作成
    indices = [Index(value = [Category.ID_KEY])],
)
data class SeriesCategoryCrossRef(
    val seriesId: Long,
    val categoryId: Long,
) {
    companion object {
        public const val TABLE_NAME = "series_category_cross_ref"
    }
}
