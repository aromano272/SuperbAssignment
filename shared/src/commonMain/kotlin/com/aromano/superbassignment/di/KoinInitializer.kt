package com.aromano.superbassignment.di

import com.aromano.superbassignment.data.di.dataModule
import com.aromano.superbassignment.presentation.di.presentationModule
import com.aromano.superbassignment.remote.di.remoteModule
import org.koin.core.context.startKoin

fun initializeKoin() {
    startKoin {
        modules(
            dataModule,
            remoteModule,
            presentationModule,
        )
    }
}
