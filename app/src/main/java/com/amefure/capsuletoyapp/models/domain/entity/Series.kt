package com.amefure.capsuletoyapp.models.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/** カプセルトイのシリーズ登録  */
@Entity(
    tableName = Series.TABLE_NAME,
)
data class Series(
    /** 主キー */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** シリーズ名 */
    var name: String,

    /** アイテム数(手入力) */
    var count: Int,

    /** 金額 */
    var amount: Int?,

    /** メモ */
    var memo: String,

    /** 画像パス */
    var imagePath: String?,

    /** 生成日 */
    val createdAt: Date = Date(),

    /** 更新日 */
    val updatedAt: Date = Date(),
) {
    companion object {
        public const val TABLE_NAME = "series_table"
        public const val ID_KEY = "seriesId"
    }
}
