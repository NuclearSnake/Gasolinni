package com.neoproduction.gasolinni.data

import androidx.lifecycle.LiveData
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

    @Query("SELECT station.text_address, station.gps, SUM(amount) as amount, SUM(price) as total FROM station JOIN refuel ON station.id = refuel.station_id GROUP BY station_id ORDER BY total")
    fun getStationsStats(): LiveData<List<StationStats>>
}