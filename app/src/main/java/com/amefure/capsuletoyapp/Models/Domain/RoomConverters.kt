package com.amefure.capsuletoyapp.Models.Domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import java.util.Date

class RoomConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromColor(color: Color): Int = color.toArgb()

    @TypeConverter
    fun toColor(value: Int): Color = Color(value)
}