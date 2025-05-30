package com.aromano.superbassignment.remote.di

import com.aromano.superbassignment.remote.DefaultProductApi
import com.aromano.superbassignment.remote.ProductApi
import org.koin.dsl.module

val remoteModule = module {

    factory<ProductApi> {
        DefaultProductApi()
    }

}