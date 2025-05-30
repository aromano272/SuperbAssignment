package com.aromano.superbassignment.presentation.di

import com.aromano.superbassignment.presentation.pos.PosArgs
import com.aromano.superbassignment.presentation.pos.PosContract
import com.aromano.superbassignment.presentation.pos.PosViewModel
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsArgs
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsContract
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsViewModel
import org.koin.dsl.module


val presentationModule = module {

    viewModelFactory<PosArgs, PosContract> { args ->
        PosViewModel(
            args = args,
            productService = get(),
        )
    }

    viewModelFactory<ProductDetailsArgs, ProductDetailsContract> { args ->
        ProductDetailsViewModel(
            args = args,
            productService = get(),
        )
    }

}