package com.aromano.superbassignment.presentation.productdetails

import com.aromano.superbassignment.domain.core.ErrorKt
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.presentation.core.Args
import com.aromano.superbassignment.presentation.core.CommonModelState
import com.aromano.superbassignment.presentation.core.CommonViewState
import com.aromano.superbassignment.presentation.core.Intent
import com.aromano.superbassignment.presentation.core.ModelStateWithCommonState
import com.aromano.superbassignment.presentation.core.Navigation
import com.aromano.superbassignment.presentation.core.ViewModel
import com.aromano.superbassignment.presentation.core.ViewStateWithCommonState

interface ProductDetailsContract : ViewModel<
        ProductDetailsArgs,
        ProductDetailsIntent,
        ProductDetailsModelState,
        ProductDetailsViewState,
        ProductDetailsNavigation,
        >

data class ProductDetailsArgs(val id: ProductId) : Args

data class ProductDetailsModelState(
    override val commonState: CommonModelState,
    val isLoading: Boolean,
    val error: ErrorKt?,
    val productId: ProductId,
    val product: Product?,
) : ModelStateWithCommonState<ProductDetailsModelState> {
    companion object {
        fun initial(productId: ProductId) = ProductDetailsModelState(
            commonState = CommonModelState(),
            isLoading = false,
            error = null,
            productId = productId,
            product = null,
        )
    }

    override fun copyCommon(commonState: CommonModelState): ProductDetailsModelState =
        copy(commonState = commonState)
}

data class ProductDetailsViewState(
    override val commonState: CommonViewState,
    val isLoading: Boolean,
    val error: String?,
    val product: Product?,
) : ViewStateWithCommonState

sealed interface ProductDetailsIntent : Intent {
    data object RefreshClicked : ProductDetailsIntent
}

sealed interface ProductDetailsNavigation : Navigation {
    data object GoBack : ProductDetailsNavigation
}

