package com.amefure.capsuletoyapp.models.domain.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.domain.entity.Series

/**
 * リレーション関係に合る各データクラスと紐づいた統合クラス
 * 一対多・・・[capsuleToys]、[locations]
 * 多対多・・・[categories]
 */
data class SeriesWithRelations(
    @Embedded val series: Series,

    /** カプセルトイ */
    @Relation(
        parentColumn = "id",
        entityColumn = Series.ID_KEY,
    )
    var capsuleToys: List<CapsuleToy>,

    /** カテゴリ */
    @Relation(
        // オリジナルSeriesクラスのID
        parentColumn = "id",
        // オリジナルCategoryクラスのID
        entityColumn = "id",
        associateBy = Junction(
            // 多対多にするためにIDを管理するクラス
            value = SeriesCategoryCrossRef::class,
            parentColumn = Series.ID_KEY,
            entityColumn = Category.ID_KEY,
        ),
    )
    var categories: List<Category>,

    /** ロケーション */
    @Relation(
        parentColumn = "id",
        entityColumn = Series.ID_KEY,
    )
    var locations: List<Location>,
)
