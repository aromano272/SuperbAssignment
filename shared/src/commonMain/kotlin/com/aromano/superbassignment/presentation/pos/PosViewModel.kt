package com.aromano.superbassignment.presentation.pos

import com.aromano.superbassignment.domain.ProductService
import com.aromano.superbassignment.domain.core.doOnFailure
import com.aromano.superbassignment.domain.core.doOnSuccess
import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.ProductSortStrategy
import com.aromano.superbassignment.presentation.core.BaseViewModel
import com.aromano.superbassignment.presentation.core.TopBarViewState
import com.aromano.superbassignment.presentation.utils.message
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class PosViewModel(
    args: PosArgs,
    private val productService: ProductService,
) : BaseViewModel<
        PosArgs,
        PosIntent,
        PosModelState,
        PosViewState,
        PosNavigation,
        >(
    args = args,
    initialModelState = PosModelState.initial
), PosContract {

    init {
        loadData()
    }

    private var loadDataJob: Job? = null
    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = launchJob {
            updateState { it.copy(isLoading = true, error = null) }
            productService.getProducts(
                sort = modelState.selectedSortStrategy,
                filterByPriceLowerThan = modelState.filterByPriceLowerThan,
            ).doOnSuccess { products ->
                updateState { it.copy(isLoading = false, products = products) }
            }.doOnFailure { error ->
                updateState { it.copy(isLoading = false, error = error) }
            }
        }
    }

    override fun mapViewState(state: PosModelState): PosViewState =
        PosViewState(
            commonState = state.commonState.toViewState(
                TopBarViewState(title = "POS")
            ),
            isLoading = state.isLoading,
            error = state.error?.message,
            products = state.products,
            availableSortStrategies = ProductSortStrategy.entries,
            selectedSortStrategy = state.selectedSortStrategy,
            priceFilterInput = state.priceFilterInput,
            priceFilterInputError = state.priceFilterInputError,
        )

    override suspend fun handleIntent(state: PosModelState, intent: PosIntent) {
        when (intent) {
            PosIntent.RefreshClicked -> loadData()
            is PosIntent.SortStrategyChanged -> {
                updateState { it.copy(products = null, selectedSortStrategy = intent.sortStrategy) }
                loadData()
            }

            is PosIntent.PriceFilterInputChanged -> priceFilterInputChanged(intent.input)
            is PosIntent.ProductClicked -> navigate(PosNavigation.GoToDetails(intent.id))
        }
    }

    private var priceFilterInputDebounceJob: Job? = null
    private fun priceFilterInputChanged(input: String) {
        if (modelState.priceFilterInput == input) return
        priceFilterInputDebounceJob?.cancel()
        priceFilterInputDebounceJob = launchJob {
            val input = input.trim()
            val parsedInput = input.toFloatOrNull()
            val inputError = input.isNotEmpty() && parsedInput == null
            updateState {
                it.copy(
                    priceFilterInput = input,
                    priceFilterInputError = inputError,
                )
            }

            if (inputError) return@launchJob

            delay(400)

            updateState {
                it.copy(
                    products = null,
                    filterByPriceLowerThan = parsedInput?.let { Cents((it * 100).toInt()) },
                )
            }

            loadData()
        }
    }
}