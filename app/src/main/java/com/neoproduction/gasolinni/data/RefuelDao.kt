package com.neoproduction.gasolinni.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RefuelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRefuel(refuel: Refuel): Long

    @Update
    fun updateRefuel(refuel: Refuel): Int

    @Query("DELETE FROM refuel WHERE id = :id")
    fun deleteRefuel(id: Int): Int

    @Query("SELECT * FROM refuel WHERE id = :id")
    fun getRefuel(id: Int): List<Refuel>

    @Query("SELECT * FROM refuel ORDER BY timestamp DESC")
    fun getRefuels(): LiveData<List<Refuel>>
}

