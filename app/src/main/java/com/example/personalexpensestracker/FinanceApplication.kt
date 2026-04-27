package com.example.personalexpensestracker

import android.app.Application
import com.example.personalexpensestracker.data.AppContainer
import com.example.personalexpensestracker.data.AppContainerImpl

class FinanceApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}