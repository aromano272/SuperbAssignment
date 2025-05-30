package com.aromano.superbassignment.data.di

import com.aromano.superbassignment.data.DefaultProductService
import com.aromano.superbassignment.domain.ProductService
import org.koin.dsl.module

val dataModule = module {

    factory<ProductService> {
        DefaultProductService(
            productApi = get(),
        )
    }

}