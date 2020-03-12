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

class AddStationViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = Repository.getInstance(app)
    private val toastLD = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = toastLD

    private val finishLD = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean> // true for success, false for cancelled
        get() = finishLD

    fun onDiscard() {
        finishLD.postValue(false)
    }

    fun onSave(parsedFields: FieldsContainer) {
        insertOrEditRefuel(parsedFields)
    }

    private fun insertOrEditRefuel(parsedFields: FieldsContainer) =
        viewModelScope.launch(Dispatchers.IO) {
            val errorMsg = getErrorMessageOrNull(parsedFields)
            if (errorMsg != null) {
                toastLD.postValue(errorMsg)
                return@launch
            }

            val stationAddress = StationAddress(parsedFields.gps, parsedFields.address)
            val stationID = findStationIdOrInsert(stationAddress)
            val refuelID = insertRefuel(stationID, stationAddress, parsedFields).toInt()

            // successful insert -> id should be some positive number
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
        return repository.insertStation(Station(0, stationAddress)).toInt()
    }

    private fun insertRefuel(
        stationID: Int, stationAddress: StationAddress,
        fieldsContainer: FieldsContainer
    ): Long = repository.insertRefuel(
        Refuel(
            0, // 0 stands for auto increment
            stationID,
            stationAddress,
            System.currentTimeMillis(),
            fieldsContainer.supplier,
            fieldsContainer.fuel,
            fieldsContainer.amount!!,
            (fieldsContainer.price!! * 100).toInt()
        )
    )
}