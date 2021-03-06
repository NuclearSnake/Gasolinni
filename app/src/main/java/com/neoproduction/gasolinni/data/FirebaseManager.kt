package com.neoproduction.gasolinni.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signInToFirebaseAndUpdate(
        stations: List<Station>,
        resultListener: ResultListener
    ) {
        // Check if already signed in
        val user = firebaseAuth.currentUser
        if (user != null) {
            Log.d("TEST", "already signed in")
            updateFirebaseDB(user, stations, resultListener)
        } else {
            Log.d("TEST", "was not signed in. signing in...")
            firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TEST", "signed in successfully")
                    val userAfterSignIn = firebaseAuth.currentUser ?: return@addOnCompleteListener
                    Log.d("TEST", "got user uid")
                    updateFirebaseDB(userAfterSignIn, stations, resultListener)
                    return@addOnCompleteListener
                }

                Log.d("TEST", "sign in failed")
                resultListener.onResult(false)
            }
        }
    }

    private fun updateFirebaseDB(
        user: FirebaseUser,
        stations: List<Station>,
        resultListener: ResultListener
    ) {
        val db = Firebase.database.reference
        Log.d("TEST", "setting value...")
        db.child("users").child(user.uid).child("stations").setValue(stations)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("TEST", "written data to the DB")
                else
                    Log.d("TEST", "Error while writing to the DB")

                resultListener.onResult(it.isSuccessful)
            }
            .addOnCanceledListener {
                Log.d("W0RK8R", "Write to DB cancelled")
                resultListener.onResult(false)
            }
    }

    interface ResultListener {
        fun onResult(result: Boolean)
    }
}