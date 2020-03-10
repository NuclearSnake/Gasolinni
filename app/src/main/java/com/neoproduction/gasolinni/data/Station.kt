package com.neoproduction.gasolinni.data

import androidx.room.*

@Entity(tableName = "station", indices = [Index(value = ["gps", "text_address"], unique = true)])
data class Station(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Embedded
    val stationAddress: StationAddress
)

data class StationAddress(
    val gps: String,
    @ColumnInfo(name = "text_address")
    val textAddress: String
)