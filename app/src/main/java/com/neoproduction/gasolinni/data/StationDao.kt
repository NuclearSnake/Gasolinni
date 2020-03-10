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

    @Query("SELECT * FROM station WHERE gps = :gps AND text_address = :textAddress")
    fun getStation(gps: String, textAddress: String): List<Station>
}