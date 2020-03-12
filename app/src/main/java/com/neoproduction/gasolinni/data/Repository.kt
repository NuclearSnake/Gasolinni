package com.neoproduction.gasolinni.data

import android.app.Application

class Repository(
    app: Application,
    private val refuelDB: RefuelRoomDB = RefuelRoomDB.getDatabase(app),
    private val refuelDao: RefuelDao = refuelDB.refuelDao(),
    private val stationDao: StationDao = refuelDB.stationDao()
) : RefuelDao by refuelDao, StationDao by stationDao