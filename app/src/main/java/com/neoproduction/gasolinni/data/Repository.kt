package com.neoproduction.gasolinni.data

import android.content.Context

class Repository private constructor(
    appContext: Context,
    private val refuelDB: RefuelRoomDB = RefuelRoomDB.getDatabase(appContext),
    private val refuelDao: RefuelDao = refuelDB.refuelDao(),
    private val stationDao: StationDao = refuelDB.stationDao()
) : RefuelDao by refuelDao, StationDao by stationDao {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        // Old good double-checked singleton
        fun getInstance(appContext: Context): Repository {
            val ins = INSTANCE
            if (ins != null)
                return ins

            synchronized(Repository::class) {
                val ins2 = INSTANCE
                if (ins2 != null)
                    return ins2

                val insFinal = Repository(appContext)
                INSTANCE = insFinal

                return insFinal
            }
        }
    }
}