package com.neoproduction.gasolinni

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.neoproduction.gasolinni.data.FirebaseManager
import com.neoproduction.gasolinni.data.Repository
import java.util.concurrent.CountDownLatch

class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), FirebaseManager.ResultListener {

    private lateinit var countDownLatch: CountDownLatch
    private lateinit var result: Result

    override fun doWork(): Result {
        val repository = Repository.getInstance(applicationContext)
        val stations = repository.getStations()

        countDownLatch = CountDownLatch(1)
        val firebaseManager = FirebaseManager()
        firebaseManager.signInToFirebaseAndUpdate(stations, this)
        countDownLatch.await()

        Log.d("W0RK8R", "Worker finished")
        return result
    }

    override fun onResult(result: Boolean) {
        this.result = if (result) Result.success() else Result.retry()
        applicationContext.setNeedSync(!result)
        Log.d("W0RK8R", "OnResult: $result")
        countDownLatch.countDown()
    }
}