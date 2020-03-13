package com.neoproduction.gasolinni.ui.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.needSync
import com.neoproduction.gasolinni.scheduleUpdateWorker
import com.neoproduction.gasolinni.ui.edit.AddGasStationActivity

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)
    val refuels = repository.getRefuels()
    val stationStats = repository.getStationsStats()

    init {
        if (app.needSync) {
            app.scheduleUpdateWorker()
        }
    }

    fun onFabClick(activity: Activity) {
        val intent = Intent(activity, AddGasStationActivity::class.java)
        activity.startActivity(intent)
    }
}