package com.neoproduction.gasolinni.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStation(station: Station): Long

    @Query("SELECT * FROM station")
    fun getStations(): List<Station>
}