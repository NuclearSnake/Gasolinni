package com.neoproduction.gasolinni.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.needSync
import com.neoproduction.gasolinni.scheduleUpdateWorker
import com.neoproduction.gasolinni.ui.edit.EditRefuelActivity
import com.neoproduction.gasolinni.ui.edit.ITEM_ID

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)
    val refuels = repository.getRefuels()
    val stationStats = repository.getStationsStats()

    init {
        if (app.needSync) {
            app.scheduleUpdateWorker()
        }
    }

    fun onFabClick(context: Context) = startEditActivity(context)

    fun onHistoryItemClicked(context: Context, id: Int?) = startEditActivity(context, id)

    private fun startEditActivity(context: Context, id: Int? = null) {
        val intent = Intent(context, EditRefuelActivity::class.java)
        if (id != null)
            intent.putExtra(ITEM_ID, id)
        context.startActivity(intent)
    }
}