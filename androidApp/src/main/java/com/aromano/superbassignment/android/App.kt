package com.aromano.superbassignment.android

import android.app.Application
import com.aromano.superbassignment.di.initializeKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin()
    }
}