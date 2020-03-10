package com.neoproduction.gasolinni.data

import androidx.room.*

@Entity(
    tableName = "refuel",
    foreignKeys = [ForeignKey(
        entity = Station::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("station_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Refuel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "station_id")
    val stationID: Int,
    @Embedded
    val stationAddress: StationAddress, // denormalization: as user never changes the station's table
    val timestamp: Long,
    val supplier: String,
    val fuel: String,
    val amount: Int,
    val price: Int
)