package com.neoproduction.gasolinni.data

import android.app.Application

class Repository(
    app: Application,
    val refuelDB: RefuelRoomDB = RefuelRoomDB.getDatabase(app),
    val refuelDao: RefuelDao = refuelDB.refuelDao(),
    val stationDao: StationDao = refuelDB.stationDao()
) : RefuelDao by refuelDao, StationDao by stationDao