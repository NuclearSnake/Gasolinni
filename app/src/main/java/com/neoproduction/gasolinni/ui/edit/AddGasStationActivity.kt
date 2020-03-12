package com.neoproduction.gasolinni.ui.edit

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.neoproduction.gasolinni.R
import kotlinx.android.synthetic.main.activity_add_gas_station.*

class AddGasStationActivity : AppCompatActivity() {
    private val vm: AddStationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gas_station)

        btnDiscard.setOnClickListener { vm.onDiscard() }
        btnSave.setOnClickListener { vm.onSave(parseFields()) }

        vm.toast.observe(this, Observer { text ->
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        })

        vm.finish.observe(this, Observer { result ->
            setResult(if (result) Activity.RESULT_OK else Activity.RESULT_CANCELED)
            finish()
        })
    }

    private fun parseFields(): FieldsContainer {
        val gps = ""
        val address = etAddress.text.toString()
        val supplier = etSupplier.text.toString()
        val fuel = etFuel.text.toString()
        val amount = etAmount.text.toString().toIntOrNull()
        val price = etPrice.text.toString().toDoubleOrNull()

        return FieldsContainer(
            gps,
            address,
            supplier,
            fuel,
            amount,
            price
        )
    }
}
