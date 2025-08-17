package com.amefure.capsuletoyapp.Models.Domain.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "capsule_toys",
    foreignKeys = [
        ForeignKey(
            entity = Series::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("seriesId")]
)
data class CapsuleToy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** シリーズID */
    val seriesId: Long,
    /** 名称 */
    val name: String,
    /** 所持フラグ */
    val isOwned: Boolean,
    /** シークレットフラグ */
    val isSecret: Boolean,
    /** メモ */
    val memo: String,
    /** 画像パス */
    val imagePath: String?,
    /** 取得日 */
    val isGetAt: Date?,
)
