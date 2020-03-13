package com.neoproduction.gasolinni

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.neoproduction.gasolinni.data.FirebaseManager
import com.neoproduction.gasolinni.data.Repository

class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val repository = Repository.getInstance(applicationContext)
        val stations = repository.getStations()

        val firebaseManager = FirebaseManager()
        firebaseManager.signInToFirebaseAndUpdate(stations)

        applicationContext.setNeedSync(false)

        return Result.success() // or Result.retry() if couldn't do it
    }
}