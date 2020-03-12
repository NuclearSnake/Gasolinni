package com.neoproduction.gasolinni.ui.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.neoproduction.gasolinni.data.Repository
import com.neoproduction.gasolinni.data.Station
import com.neoproduction.gasolinni.ui.edit.AddGasStationActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val repository = Repository.getInstance(app)
    val refuels = repository.getRefuels()
    val stationStats = repository.getStationsStats()

    fun onStationsChange() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("TEST", "on stations changed")
        val stations = repository.getStations()
        signInToFirebaseAndUpdate(stations)
    }

    private fun signInToFirebaseAndUpdate(stations: List<Station>) {
        // Check if already signed in
        val user = firebaseAuth.currentUser
        if (user != null) {
            Log.d("TEST", "already signed in")
            updateFirebaseDB(user, stations)
        } else {
            Log.d("TEST", "was not signed in. signing in...")
            firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TEST", "signed in successfully")
                    val userAfterSignIn = firebaseAuth.currentUser ?: return@addOnCompleteListener
                    Log.d("TEST", "got user uid")
                    updateFirebaseDB(userAfterSignIn, stations)
                    return@addOnCompleteListener
                }

                Log.d("TEST", "sign in failed")
            }
        }
    }

    private fun updateFirebaseDB(user: FirebaseUser, stations: List<Station>) {
        val db = Firebase.database.reference
        Log.d("TEST", "setting value")
        db.child("users").child(user.uid).child("stations").setValue(stations)
            .addOnCompleteListener {
                Log.d("TEST", "written data to the DB")
            }
    }

    fun onFabClick(activity: Activity) {
        val intent = Intent(activity, AddGasStationActivity::class.java)
        activity.startActivity(intent)
    }
}