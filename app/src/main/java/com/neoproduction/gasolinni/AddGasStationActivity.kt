package com.neoproduction.gasolinni

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
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

        btnDiscard.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btnSave.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                insertRefuel()
            }
        }
    }

    class FieldsContainer(
        val gps: String?,
        val address: String?,
        val supplier: String,
        val fuel: String,
        val amount: Int,
        val price: Int
    )

    private fun tryParseFields(): FieldsContainer? {
        val gps = ""
        val address = etAddress.text.toString()
        val supplier = etSupplier.text.toString()
        val fuel = etFuel.text.toString()
        val amount = etAmount.text.toString().toIntOrNull()
        val price = etPrice.text.toString().toDoubleOrNull()

        var errorMessage: String? = null
        // TODO: Error indication with positioning on editText with the error
        if (address.isBlank() && gps.isBlank())
            errorMessage = "No address provided"
        else if (supplier.isBlank())
            errorMessage = "Supplier not specified"
        else if (fuel.isBlank())
            errorMessage = "Fuel not specified"
        else if (amount == null)
            errorMessage = "Wrong amount"
        else if (price == null)
            errorMessage = "Incorrect price"

        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            return null
        }

        // amount and price can't be null here
        return FieldsContainer(gps, address, supplier, fuel, amount!!, (price!! * 100).toInt())
    }


    private suspend fun insertRefuel() {
        val fieldsContainer = tryParseFields() ?: return

        val refuelDB = RefuelRoomDB.getDatabase(this)
        val refuelDao = refuelDB.refuelDao()
        val stationDao = refuelDB.stationDao()

        var stations: List<Station> = listOf()
        withContext(Dispatchers.IO) {
            stations = stationDao.getStations()
        }
        var id = 0
        for (station in stations) {
            if (!fieldsContainer.gps.isNullOrBlank() && fieldsContainer.gps == station.gps) {
                id = station.id
                break
            }

            if (!fieldsContainer.address.isNullOrBlank() && fieldsContainer.address == station.textAddress) {
                id = station.id
                break
            }
        }

        if (id == 0) {
            withContext(Dispatchers.IO) {
                id = stationDao.insertStation(
                    Station(
                        id,
                        fieldsContainer.gps,
                        fieldsContainer.address
                    )
                ).toInt()
            }
        }

        var refuelID = -1
        withContext(Dispatchers.IO) {
            // 0 stands for not-set as ID
            refuelID = refuelDao.insertRefuel(
                Refuel(
                    0,
                    id,
                    System.currentTimeMillis(),
                    fieldsContainer.supplier,
                    fieldsContainer.fuel,
                    fieldsContainer.amount,
                    fieldsContainer.price
                )
            ).toInt()
        }

        if (refuelID == -1) {
            Toast.makeText(
                this,
                "Error while saving data. Please try again later",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
