package com.neoproduction.gasolinni.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.needSync
import com.neoproduction.gasolinni.scheduleUpdateWorker
import com.neoproduction.gasolinni.ui.edit.EditRefuelActivity
import com.neoproduction.gasolinni.ui.edit.ITEM_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)
    val refuels = repository.getRefuels()
    val stationStats = repository.getStationsStats()

    private val fabVisibilityLD = MutableLiveData(true)
    val fabVisibility: LiveData<Boolean>
        get() = fabVisibilityLD

    init {
        if (app.needSync) {
            app.scheduleUpdateWorker()
        }
    }

    fun onFabClick(context: Context) = startEditActivity(context)

    fun onEditItem(context: Context, id: Int?) = startEditActivity(context, id)
    fun onDeleteItem(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRefuel(id)
    }

    fun onScrollStateChanged(scrolling: Boolean) {
        fabVisibilityLD.value = !scrolling
    }


    private fun startEditActivity(context: Context, id: Int? = null) {
        val intent = Intent(context, EditRefuelActivity::class.java)
        if (id != null)
            intent.putExtra(ITEM_ID, id)
        context.startActivity(intent)
    }
}