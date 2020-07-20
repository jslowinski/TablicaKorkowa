package com.example.tablicakorkowa

import android.app.Application
import timber.log.Timber

class TablicaKorkowaApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}