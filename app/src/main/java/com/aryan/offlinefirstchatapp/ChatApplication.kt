package com.aryan.offlinefirstchatapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this,config)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
}