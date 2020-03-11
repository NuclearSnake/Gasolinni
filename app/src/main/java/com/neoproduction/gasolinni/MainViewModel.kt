package com.neoproduction.gasolinni

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    fun onFabClick(activity: Activity) {
        val intent = Intent(activity, AddGasStationActivity::class.java)
        activity.startActivity(intent)
    }
}