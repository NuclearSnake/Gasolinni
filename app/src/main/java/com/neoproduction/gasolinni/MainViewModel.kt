package com.neoproduction.gasolinni

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.neoproduction.gasolinni.data.Repository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository(app)
    val refuels = repository.getRefuels()
    val stationStats = repository.getStationsStats()

    fun onFabClick(activity: Activity) {
        val intent = Intent(activity, AddGasStationActivity::class.java)
        activity.startActivity(intent)
    }

//    private fun updateRefuelsHistory() = viewModelScope.launch(Dispatchers.IO) {
//        repository.getRefuels()
//    }
}