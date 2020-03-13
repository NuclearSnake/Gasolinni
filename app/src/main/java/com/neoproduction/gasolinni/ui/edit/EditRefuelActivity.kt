package com.neoproduction.gasolinni.ui.edit

import android.app.Activity
import android.graphics.Color
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
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.neoproduction.gasolinni.R
import com.neoproduction.gasolinni.data.Refuel
import com.neoproduction.gasolinni.scheduleUpdateWorker
import com.neoproduction.gasolinni.setNeedSync
import com.neoproduction.gasolinni.toDoublePrice
import kotlinx.android.synthetic.main.activity_edit_refuel.*


class EditRefuelActivity : AppCompatActivity() {
    private val vm: EditRefuelViewModel by viewModels()
    private lateinit var mapboxMap: MapboxMap
    private var coords: LatLng? = null
    private var editable: Refuel? = null
    private var isMapInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.onActivityCreated(intent)

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the map view.
        Mapbox.getInstance(this, MapboxInfo.token)
        setContentView(R.layout.activity_edit_refuel)

        setupMap()

        btnDiscard.setOnClickListener { vm.onDiscard() }
        btnSave.setOnClickListener { vm.onSave(parseFields(), editable) }

        vm.toast.observe(this, Observer { text ->
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        })

        vm.finish.observe(this, Observer { result ->
            setResult(if (result) Activity.RESULT_OK else Activity.RESULT_CANCELED)
            finish()
        })

        vm.scheduleUpdate.observe(this, Observer {
            setNeedSync(true) // important!
            scheduleUpdateWorker()
        })

        vm.refuelToEdit.observe(this, Observer {
            fillTheFields(it)
            editable = it
        })
    }

    private fun setupMap() {
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

                isMapInitialized = true
                applyCoordinates(coords)
            }
        }
    }

    private fun applyCoordinates(coords: LatLng?) {
        if (!isMapInitialized)
            return

        if (coords != null) {
            mapboxMap.cameraPosition = CameraPosition.Builder()
                .target(coords)
                .zoom(14.0)
                .build()

            moveMarker(coords)
            mapView.isEnabled = false
        } else {
            mapView.isEnabled = true
            mapboxMap.addOnMapClickListener(::onMapClicked)
        }
    }

    private fun onMapClicked(coords: LatLng): Boolean {
        tvGps.text = getString(R.string.placeh_gps, coords.latitude, coords.longitude)
        moveMarker(coords)
        this.coords = coords
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

    private fun fillTheFields(refuel: Refuel?) {
        if (refuel != null) {
            etAddress.isEnabled = false

            etAddress.setText(refuel.stationAddress.textAddress)
            etAmount.setText(refuel.amount.toString())
            etPrice.setText(refuel.price.toDoublePrice().toString())
            etFuel.setText(refuel.fuel)
            etSupplier.setText(refuel.supplier)
            tvGps.text = refuel.stationAddress.gps
            tvGps.setTextColor(Color.rgb(200, 200, 200))

            if (refuel.stationAddress.gps.isNotBlank()) {
                val list = refuel.stationAddress.gps.split(" ")
                val coords = LatLng(list[0].toDouble(), list[1].toDouble())

                applyCoordinates(coords)

                this.coords = coords
            }

            mapView.isEnabled = false
            Toast.makeText(this, "Note that you can't change address on edit", Toast.LENGTH_LONG)
                .show()
        } else {
            mapView.isEnabled = true
        }
    }

    private fun parseFields(): FieldsContainer {
        val finalCoords = coords
        val gps =
            if (finalCoords == null) "" else "${finalCoords.latitude} ${finalCoords.longitude}"
        val address = etAddress.text.toString()
        val supplier = etSupplier.text.toString()
        val fuel = etFuel.text.toString()
        val amount = etAmount.text.toString().toIntOrNull()
        val price = etPrice.text.toString().toDoubleOrNull()

        return FieldsContainer(gps, address, supplier, fuel, amount, price)
    }

    // Mapbox requires all the lifecycle events to be forwarded. Unfortunately, the provide no
    // compatibility with the Activity.getLifecycle() methods

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
