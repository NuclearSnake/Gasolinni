package com.neoproduction.gasolinni.ui.edit

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoproduction.gasolinni.data.Refuel
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.data.Station
import com.neoproduction.gasolinni.data.StationAddress
import com.neoproduction.gasolinni.toPriceInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ITEM_ID = "item_id"

class EditRefuelViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)

    private val toastLD = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = toastLD

    private val finishLD = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean> // true for success, false for cancelled
        get() = finishLD

    private val scheduleUpdateLD = MutableLiveData<Boolean>()
    val scheduleUpdate: LiveData<Boolean> // true for success, false for cancelled
        get() = scheduleUpdateLD

    private val refuelToEditLD = MutableLiveData<Refuel?>()
    val refuelToEdit: LiveData<Refuel?>
        get() = refuelToEditLD

    fun onActivityCreated(intent: Intent) = viewModelScope.launch(Dispatchers.IO) {
        val id = intent.getIntExtra(ITEM_ID, -1)
        if (id != -1) {
            val refuels = repository.getRefuel(id)
            if (refuels.isNotEmpty()) {
                refuelToEditLD.postValue(refuels[0])
                return@launch
            }
        }

        refuelToEditLD.postValue(null)
    }

    fun onDiscard() {
        finishLD.postValue(false)
    }

    fun onSave(parsedFields: FieldsContainer, original: Refuel?) {
        insertOrUpdateRefuel(parsedFields, original)
    }

    // ------------- PRIVATE FUNCTIONS --------------

    private fun onStationsChange() {
        scheduleUpdateLD.postValue(true)
    }

    private fun insertOrUpdateRefuel(parsedFields: FieldsContainer, original: Refuel?) =
        viewModelScope.launch(Dispatchers.IO) {
            val errorMsg = getErrorMessageOrNull(parsedFields)
            if (errorMsg != null) {
                toastLD.postValue(errorMsg)
                return@launch
            }

            val stationAddress = StationAddress(parsedFields.gps, parsedFields.address)
            val stationID = original?.stationID ?: findStationIdOrInsert(stationAddress)

            val refuel = parsedFields.toRefuel(stationID, stationAddress, original)
            val refuelID = if (refuel.id == 0)
                repository.insertRefuel(refuel).toInt()
            else
                repository.updateRefuel(refuel)

            // successful insert or update -> id should be some positive number
            if (refuelID == -1) {
                toastLD.postValue("Error while saving data. Please try again later")
            } else {
                toastLD.postValue("Saved")
                finishLD.postValue(true)
            }
        }

    /**
     * Checks parsed earlier fields from FieldsContainer and returns
     * the appropriate error message or null if all fields are correct
     */
    private fun getErrorMessageOrNull(parsedFields: FieldsContainer): String? {
        var errorMessage: String? = null
        // TODO: Error indication with positioning on editText with the error
        if (parsedFields.address.isBlank() && parsedFields.gps.isBlank())
            errorMessage = "No address provided"
        else if (parsedFields.supplier.isBlank())
            errorMessage = "Supplier not specified"
        else if (parsedFields.fuel.isBlank())
            errorMessage = "Fuel not specified"
        else if (parsedFields.amount == null)
            errorMessage = "Wrong amount"
        else if (parsedFields.price == null)
            errorMessage = "Incorrect price"

        return errorMessage // null if nothing suspicious found
    }

    private fun findStationIdOrInsert(stationAddress: StationAddress): Int {
        val stationsFound: List<Station> =
            repository.getStation(stationAddress.gps, stationAddress.textAddress)

        if (stationsFound.isNotEmpty())
            return stationsFound[0].id

        // If not found - need to insert
        // id = 0 stands for auto increment
        val newId = repository.insertStation(Station(0, stationAddress)).toInt()
        onStationsChange()
        return newId
    }

    private fun FieldsContainer.toRefuel(
        stationID: Int,
        stationAddress: StationAddress,
        original: Refuel?
    ) = Refuel(
        original?.id ?: 0, // 0 stands for auto increment
        stationID,
        stationAddress,
        original?.timestamp ?: System.currentTimeMillis(),
        supplier,
        fuel,
        amount!!,
        price!!.toPriceInt()
    )
}