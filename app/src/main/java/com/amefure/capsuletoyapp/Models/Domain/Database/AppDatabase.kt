package com.amefure.capsuletoyapp.Models.Domain.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amefure.capsuletoyapp.Models.Domain.Dao.CapsuleToyDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.CategoryDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.LocationDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.SeriesCategoryCrossRefDao
import com.amefure.capsuletoyapp.Models.Domain.Dao.SeriesDao
import com.amefure.capsuletoyapp.Models.Domain.Entity.CapsuleToy
import com.amefure.capsuletoyapp.Models.Domain.Entity.Category
import com.amefure.capsuletoyapp.Models.Domain.Entity.Location
import com.amefure.capsuletoyapp.Models.Domain.Entity.Relation.SeriesCategoryCrossRef
import com.amefure.capsuletoyapp.Models.Domain.Entity.Series
import com.amefure.capsuletoyapp.Models.Domain.RoomConverters

@Database(entities =
    [
        Series::class,
        CapsuleToy::class,
        Category::class,
        Location::class,
        SeriesCategoryCrossRef::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun seriesDao(): SeriesDao
    abstract fun capsuleToyDao(): CapsuleToyDao
    abstract fun locationDao(): LocationDao
    abstract fun categoryDao(): CategoryDao
    abstract fun seriesCategoryCrossRefDao(): SeriesCategoryCrossRefDao

    companion object {

        private const val DATABASE_NAME = "app_database"

        // @Volatile => メモリ保存
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** データベース取得 */
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