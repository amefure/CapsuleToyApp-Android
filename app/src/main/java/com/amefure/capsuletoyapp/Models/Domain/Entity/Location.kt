package com.amefure.capsuletoyapp.Models.Domain.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "locations",
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
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** シリーズID */
    val seriesId: Long,
    /** 名称 */
    val name: String,
    /** 緯度 (オプション) */
    var latitude: Double?,
    /** 経度 (オプション) */
    var longitude: Double?
)
