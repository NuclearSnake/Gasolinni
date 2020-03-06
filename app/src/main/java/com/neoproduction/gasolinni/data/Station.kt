package com.neoproduction.gasolinni.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val gps: String? = null,
    val textAddress: String? = null
)