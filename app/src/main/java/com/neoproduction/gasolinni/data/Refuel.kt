package com.neoproduction.gasolinni.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val timestamp: Long,
    val supplier: String,
    val fuel: String,
    val amount: Int,
    val price: Int
)