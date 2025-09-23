package com.amefure.capsuletoyapp.models.domain.entity.relation

import android.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amefure.capsuletoyapp.models.domain.entity.CapsuleToy
import com.amefure.capsuletoyapp.models.domain.entity.Category
import com.amefure.capsuletoyapp.models.domain.entity.Location
import com.amefure.capsuletoyapp.models.domain.entity.Series
import java.util.Date

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

object SeriesMockFactory {

    private fun mock(
        name: String = "猫フィギュア Vol.1",
        count: Int = 5,
        amount: Int = 300,
        capsuleToyNames: List<String> = listOf("黒猫", "白猫", "トラ猫", "三毛猫", "キジトラ"),
        categoryNames: List<String> = listOf("鬼滅の刃"),
        categoryColors: List<Int> = listOf(Color.RED),
        memo: String = "お気に入りシリーズ",
    ): SeriesWithRelations {
        val series = Series(
            id = 0,
            name = name,
            count = count,
            amount = amount,
            memo = memo,
            createdAt = Date(),
            updatedAt = Date(),
            imagePath = null,
        )

        val capsuleToys = capsuleToyNames.map { toyName ->
            CapsuleToy(
                id = 0,
                seriesId = series.id,
                name = toyName,
                isOwned = listOf(true, false).random(),
                isSecret = listOf(true, false).random(),
                memo = "",
                imagePath = null,
                isGetAt = if (Math.random() > 0.5) Date() else null,
            )
        }

        val categories = categoryNames.mapIndexed { index, categoryName ->
            Category(
                id = 0,
                name = categoryName,
                colorArgb = categoryColors.getOrNull(index) ?: Color.BLACK,
            )
        }

        val locations = emptyList<Location>() // 必要なら追加

        return SeriesWithRelations(
            series = series,
            capsuleToys = capsuleToys,
            categories = categories,
            locations = locations,
        )
    }

    fun mockList(): List<SeriesWithRelations> {
        return listOf(
            mock(
                name = "海の仲間シリーズ",
                count = 5,
                amount = 300,
                capsuleToyNames = listOf("イルカ", "クジラ", "カクレクマノミ", "タツノオトシゴ", "カメ"),
                categoryNames = listOf("海の生き物", "水族館"),
                categoryColors = listOf("#1E90FF".toColorInt(), "#00CED1".toColorInt()),
                memo = "海モチーフのカプセルトイ",
            ),
            mock(
                name = "恐竜ザウルス Vol.1",
                count = 4,
                amount = 500,
                capsuleToyNames = listOf("ティラノサウルス", "トリケラトプス", "ステゴサウルス", "プテラノドン"),
                categoryNames = listOf("恐竜", "古代生物"),
                categoryColors = listOf("#8B4513".toColorInt(), "#556B2F".toColorInt()),
                memo = "恐竜好き必見",
            ),
            mock(
                name = "ミニチュア家具シリーズ",
                count = 3,
                amount = 300,
                capsuleToyNames = listOf("ソファ", "ダイニングテーブル", "ランプ"),
                categoryNames = listOf("家具", "インテリア"),
                categoryColors = listOf("#CD853F".toColorInt(), "#D2B48C".toColorInt()),
                memo = "小物ディスプレイ用にも最適",
            ),
        )
    }
}
