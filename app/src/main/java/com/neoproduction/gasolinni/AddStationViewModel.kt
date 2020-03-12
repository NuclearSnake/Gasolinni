package com.neoproduction.gasolinni

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoproduction.gasolinni.data.Refuel
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.data.Station
import com.neoproduction.gasolinni.data.StationAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddStationViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)
    private val toastLD = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = toastLD

    private val finishLD = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean>
        get() = finishLD

    fun onDiscard() {
        finishLD.postValue(false)
    }

    fun onSave(parsedFields: FieldsContainer?) {
        insertOrEditRefuel(parsedFields)
    }

    private fun insertOrEditRefuel(parsedFields: FieldsContainer?) =
        viewModelScope.launch(Dispatchers.IO) {
            val fieldsContainer = parsedFields ?: return@launch

            val stationAddress = StationAddress(
                fieldsContainer.gps ?: "",
                fieldsContainer.address ?: ""
            )

            val stationID = findStationIdOrInsert(stationAddress)
            var refuelID = -1
            withContext(Dispatchers.IO) {
                // 0 stands for not-set as ID
                refuelID = repository.insertRefuel(
                    Refuel(
                        0,
                        stationID,
                        stationAddress,
                        System.currentTimeMillis(),
                        fieldsContainer.supplier,
                        fieldsContainer.fuel,
                        fieldsContainer.amount,
                        fieldsContainer.price
                    )
                ).toInt()
            }

            // successful insert -> id should be some positive number
            if (refuelID == -1) {
                toastLD.postValue("Error while saving data. Please try again later")
            } else {
                toastLD.postValue("Saved")
                finishLD.postValue(true)
            }
        }

    private fun findStationIdOrInsert(stationAddress: StationAddress): Int {
        val stationsFound: List<Station> =
            repository.getStation(stationAddress.gps, stationAddress.textAddress)

        if (stationsFound.isNotEmpty())
            return stationsFound[0].id

        // If not found - need to insert
        return repository.insertStation(Station(0, stationAddress))
            .toInt() // id = 0 stands for auto_increment
    }
}