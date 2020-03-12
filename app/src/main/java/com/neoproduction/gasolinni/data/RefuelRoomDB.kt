package com.neoproduction.gasolinni.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Station::class, Refuel::class], version = 3, exportSchema = true)
abstract class RefuelRoomDB : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun refuelDao(): RefuelDao

    companion object {
        // We need singleton instance to prevent multiple DBs opening since it's expensive
        @Volatile
        private var INSTANCE: RefuelRoomDB? = null

        // Old good double-checked singleton
        fun getDatabase(context: Context): RefuelRoomDB {
            val ins = INSTANCE
            if (ins != null)
                return ins

            synchronized(RefuelRoomDB::class) {
                val ins2 = INSTANCE
                if (ins2 != null)
                    return ins2

                val insFinal = Room.databaseBuilder(
                    context.applicationContext,
                    RefuelRoomDB::class.java,
                    "refuel_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = insFinal

                return insFinal
            }
        }
    }
}