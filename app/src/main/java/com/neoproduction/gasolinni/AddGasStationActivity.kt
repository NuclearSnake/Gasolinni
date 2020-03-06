package com.neoproduction.gasolinni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neoproduction.gasolinni.data.Refuel
import com.neoproduction.gasolinni.data.RefuelRoomDB
import com.neoproduction.gasolinni.data.Station
import kotlinx.android.synthetic.main.activity_add_gas_station.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGasStationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gas_station)

        tvText.text = "Building new station... please wait..."

        GlobalScope.launch(Dispatchers.Main) {
            doMockUpInserts()
        }
    }

    suspend fun doMockUpInserts() {
        val refuelDB = RefuelRoomDB.getDatabase(this)
        val refuelDao = refuelDB.refuelDao()
        val stationDao = refuelDB.stationDao()

        withContext(Dispatchers.IO) {
            stationDao.insertStation(Station(1, null, "area 51"))
        }

        tvText.append("\nStation built! Refueling...")

        withContext(Dispatchers.IO) {
            // 0 stands for not-set as ID
            refuelDao.insertRefuel(
                Refuel(
                    0,
                    1,
                    System.currentTimeMillis(),
                    "Supplier X",
                    "Alien 44",
                    100,
                    150
                )
            )
        }
        tvText.append("\nRefueled successfully! Ready to go!")
    }
}
