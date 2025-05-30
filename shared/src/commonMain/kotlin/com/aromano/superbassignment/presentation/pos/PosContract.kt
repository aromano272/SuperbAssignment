package com.aromano.superbassignment.presentation.pos

import com.aromano.superbassignment.domain.core.ErrorKt
import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.domain.model.ProductSortStrategy
import com.aromano.superbassignment.presentation.core.Args
import com.aromano.superbassignment.presentation.core.CommonModelState
import com.aromano.superbassignment.presentation.core.CommonViewState
import com.aromano.superbassignment.presentation.core.Intent
import com.aromano.superbassignment.presentation.core.ModelStateWithCommonState
import com.aromano.superbassignment.presentation.core.Navigation
import com.aromano.superbassignment.presentation.core.ViewModel
import com.aromano.superbassignment.presentation.core.ViewStateWithCommonState

interface PosContract : ViewModel<
        PosArgs,
        PosIntent,
        PosModelState,
        PosViewState,
        PosNavigation,
        >

data object PosArgs : Args

data class PosModelState(
    override val commonState: CommonModelState,
    val isLoading: Boolean,
    val error: ErrorKt?,
    val products: List<Product>?,
    val selectedSortStrategy: ProductSortStrategy,
    val priceFilterInput: String,
    val priceFilterInputError: Boolean,
    val filterByPriceLowerThan: Cents?,
) : ModelStateWithCommonState<PosModelState> {
    companion object {
        val initial = PosModelState(
            commonState = CommonModelState(),
            isLoading = false,
            error = null,
            products = null,
            selectedSortStrategy = ProductSortStrategy.NAME_ASC,
            priceFilterInput = "",
            priceFilterInputError = false,
            filterByPriceLowerThan = null,
        )
    }

    override fun copyCommon(commonState: CommonModelState): PosModelState =
        copy(commonState = commonState)
}

data class PosViewState(
    override val commonState: CommonViewState,
    val isLoading: Boolean,
    val error: String?,
    val products: List<Product>?,
    val availableSortStrategies: List<ProductSortStrategy>,
    val selectedSortStrategy: ProductSortStrategy,
    val priceFilterInput: String,
    val priceFilterInputError: Boolean,
) : ViewStateWithCommonState

sealed interface PosIntent : Intent {
    data object RefreshClicked : PosIntent
    data class SortStrategyChanged(val sortStrategy: ProductSortStrategy) : PosIntent
    data class PriceFilterInputChanged(val input: String) : PosIntent
    data class ProductClicked(val id: ProductId) : PosIntent
}

sealed interface PosNavigation : Navigation {
    data class GoToDetails(val id: ProductId) : PosNavigation
}

