package com.example.nearbyplaces.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nearbyplaces.model.Restaurants
import com.example.nearbyplaces.util.Constants.Companion.DATABASE_NAME


@Database(entities = [Restaurants::class], version = 1, exportSchema = false)
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
            DATABASE_NAME
        ).build()
    }
}