package com.amefure.capsuletoyapp.Models.Domain.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amefure.capsuletoyapp.Models.Domain.Dao.SeriesDao
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series

@Database(entities =
    [
        Series::class,
        CapsuleToy::class,
        Category::class,
        Location::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seriesDao(): SeriesDao

    companion object {

        private const val DATABASE_NAME = "app_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}