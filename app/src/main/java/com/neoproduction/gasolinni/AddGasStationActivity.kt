package com.neoproduction.gasolinni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_gas_station.*

class AddGasStationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gas_station)

        tvText.text = "Building new station... please wait..."
    }
}
