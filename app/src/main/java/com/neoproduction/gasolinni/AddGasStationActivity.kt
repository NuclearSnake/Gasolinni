package com.neoproduction.gasolinni

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_add_gas_station.*

class AddGasStationActivity : AppCompatActivity() {
    val vm: AddStationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gas_station)

        btnDiscard.setOnClickListener { vm.onDiscard() }
        btnSave.setOnClickListener { vm.onSave(tryParseFields()) }

        vm.toast.observe(this, Observer { text ->
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        })

        vm.finish.observe(this, Observer { result ->
            setResult(if (result) Activity.RESULT_OK else Activity.RESULT_CANCELED)
            finish()
        })
    }

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
}
