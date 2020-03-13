package com.neoproduction.gasolinni.ui.edit

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.neoproduction.gasolinni.R
import kotlinx.android.synthetic.main.activity_add_gas_station.*


class AddGasStationActivity : AppCompatActivity() {
    private val vm: AddStationViewModel by viewModels()
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, MapboxInfo.token)
        setContentView(R.layout.activity_add_gas_station)

        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                style.addSource(GeoJsonSource("source-id"))

                style.addLayer(
                    SymbolLayer("layer-id", "source-id").withProperties(
                        iconImage("fuel-15"),
                        iconIgnorePlacement(true),
                        iconAllowOverlap(true),
                        iconSize(2f)
                    )
                )
            }
            mapboxMap.addOnMapClickListener(::onMapClicked)
        }


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

    private fun onMapClicked(coords: LatLng): Boolean {
        tvGps.text = getString(R.string.placeh_gps, coords.latitude, coords.longitude)
        moveMarker(coords)
        return true
    }

    private fun moveMarker(newCoords: LatLng) {
        val source: GeoJsonSource? = mapboxMap.style?.getSourceAs("source-id")
        source?.setGeoJson(
            FeatureCollection.fromFeature(
                Feature.fromGeometry(Point.fromLngLat(newCoords.longitude, newCoords.latitude))
            )
        )
    }

    private fun parseFields(): FieldsContainer {
        val gps = tvGps.text.toString()
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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }
}
