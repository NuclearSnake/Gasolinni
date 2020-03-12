package com.neoproduction.gasolinni.data

import android.app.Application

class Repository private constructor(
    app: Application,
    private val refuelDB: RefuelRoomDB = RefuelRoomDB.getDatabase(app),
    private val refuelDao: RefuelDao = refuelDB.refuelDao(),
    private val stationDao: StationDao = refuelDB.stationDao()
) : RefuelDao by refuelDao, StationDao by stationDao {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        // Old good double-checked singleton
        fun getInstance(app: Application): Repository {
            val ins = INSTANCE
            if (ins != null)
                return ins

            synchronized(Repository::class) {
                val ins2 = INSTANCE
                if (ins2 != null)
                    return ins2

                val insFinal = Repository(app)
                INSTANCE = insFinal

                return insFinal
            }
        }
    }
}