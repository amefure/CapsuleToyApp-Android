package com.amefure.capsuletoyapp.models.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = CapsuleToy.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Series::class,
            parentColumns = ["id"],
            childColumns = [Series.ID_KEY],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(Series.ID_KEY)],
)
data class CapsuleToy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** シリーズID */
    val seriesId: Long,
    /** 名称 */
    var name: String,
    /** 所持フラグ */
    var isOwned: Boolean,
    /** シークレットフラグ */
    var isSecret: Boolean,
    /** メモ */
    var memo: String,
    /** 画像パス */
    var imagePath: String?,
    /** 取得日 */
    var isGetAt: Date?,
) {
    companion object {
        public const val TABLE_NAME = "capsule_toys"
    }
}
