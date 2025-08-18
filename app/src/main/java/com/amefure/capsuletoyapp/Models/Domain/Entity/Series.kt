package com.amefure.capsuletoyapp.Models.Domain.Entity

import androidx.room.*
import java.util.Date

/** カプセルトイのシリーズ登録  */
@Entity(
    tableName = Series.TABLE_NAME
)
data class Series(
    /** 主キー */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** シリーズ名 */
    val name: String,

    /** アイテム数(手入力) */
    val count: Int,

    /** 金額 */
    val amount: Int,

    /** メモ */
    val memo: String,

    /** 画像パス */
    val imagePath: String?,

    /** 生成日 */
    val createdAt: Date = Date(),

    /** 更新日 */
    val updatedAt: Date = Date()
) {
    companion object {
        public const val TABLE_NAME = "series_table"
    }
}