package com.amefure.capsuletoyapp.models.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = Location.TABLE_NAME,
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
    var longitude: Double?,
) : Serializable {
    companion object {
        public const val TABLE_NAME = "locations"
        public const val KEY = "location"
    }
}
