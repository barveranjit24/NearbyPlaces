package com.example.nearbyplaces.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyplaces.model.Restaurants

@Dao
interface RestaurantsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<Restaurants?>?): List<Long>

    @Query("SELECT * FROM restaurants")
    suspend fun getAllRestaurants(): List<Restaurants>

    @Query("DELETE FROM restaurants")
    suspend fun deleteAllRestaurants()

}