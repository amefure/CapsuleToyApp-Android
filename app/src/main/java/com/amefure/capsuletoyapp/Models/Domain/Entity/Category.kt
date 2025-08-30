package com.amefure.capsuletoyapp.Models.Domain.Entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Colorはプリミティブ型ではないため保存できないため
 * RGBA値で保存しておく
 * Navigationでも値渡しするのでConvertersではなくSerializableにして
 */
@Entity(
    tableName = "categories",
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    /** カテゴリ名 */
    val name: String,
    /** 色(RGBA) */
    val colorArgb: Int
): Serializable {
    val color: Color
        get() = Color(colorArgb)

    companion object {
       const val KEY = "category"
    }
}