package com.neoproduction.gasolinni.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RefuelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRefuel(refuel: Refuel)

    @Query("SELECT * FROM refuel")
    fun getRefuels(): List<Refuel>

}

