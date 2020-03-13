package com.neoproduction.gasolinni

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

const val SHARED_PREFERENCE_FILE_NAME = "SharedPrefsSync"
const val NEED_SYNC_KEY = "need_sync"

fun Context.scheduleUpdateWorker() {
    val constrains = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val workRequest = OneTimeWorkRequestBuilder<UpdateWorker>()
        .setConstraints(constrains)
        .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

/**
 * Needed because on some devices when app is force-stopped the workers
 * got killed and need to be restarted on app start
 */
fun Context.setNeedSync(value: Boolean) {
    getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(NEED_SYNC_KEY, value)
        .apply()
}

val Context.needSync: Boolean
    get() = getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        .getBoolean(NEED_SYNC_KEY, false)