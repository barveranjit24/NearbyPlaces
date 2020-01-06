package com.example.nearbyplaces.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nearbyplaces.model.Restaurants


@Database(entities = [Restaurants::class], version = 1, exportSchema = false)
//@TypeConverters(OpeningHourDataTypeConverter::class)
abstract class RestaurantsDatabase: RoomDatabase() {
    abstract fun restaurantsDao(): RestaurantsDao

    companion object {
        @Volatile private var instance: RestaurantsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RestaurantsDatabase::class.java,
            "restaurantsdatabase"
        ).build()
    }
}