package com.example.personalexpensestracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

//import com.example.personalexpensestracker.data.AppContainer
//import com.example.personalexpensestracker.data.AppContainerImpl
//import com.example.personalexpensestracker.worker.FinanceWorkersFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class FinanceApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}