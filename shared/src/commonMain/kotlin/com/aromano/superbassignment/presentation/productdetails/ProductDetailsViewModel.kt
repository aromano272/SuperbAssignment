package com.aromano.superbassignment.presentation.productdetails

import com.aromano.superbassignment.domain.ProductService
import com.aromano.superbassignment.domain.core.ErrorKt
import com.aromano.superbassignment.domain.core.doOnFailure
import com.aromano.superbassignment.domain.core.doOnSuccess
import com.aromano.superbassignment.presentation.core.BaseViewModel
import com.aromano.superbassignment.presentation.core.TopBarViewState
import com.aromano.superbassignment.presentation.utils.Strings
import com.aromano.superbassignment.presentation.utils.message
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class ProductDetailsViewModel(
    args: ProductDetailsArgs,
    private val productService: ProductService,
) : BaseViewModel<
        ProductDetailsArgs,
        ProductDetailsIntent,
        ProductDetailsModelState,
        ProductDetailsViewState,
        ProductDetailsNavigation,
        >(
    args = args,
    initialModelState = ProductDetailsModelState.initial(args.id),
), ProductDetailsContract {

    init {
        loadData()

        launch {
            delay(5000)
            showErrorAlert(ErrorKt.NotFound)
            delay(10000)
            showSuccessAlert("Success!")
        }
    }

    private var loadDataJob: Job? = null
    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = launchJob {
            updateState { it.copy(isLoading = true, error = null) }
            productService.getProductById(args.id)
                .doOnSuccess { product ->
                    updateState { it.copy(isLoading = false, product = product) }
                }.doOnFailure { error ->
                    updateState { it.copy(isLoading = false, error = error) }
                }
        }
    }

    override fun mapViewState(state: ProductDetailsModelState): ProductDetailsViewState =
        ProductDetailsViewState(
            commonState = state.commonState.toViewState(
                topBarViewState = TopBarViewState(
                    title = Strings.product_details_topbar,
                    onBackHandler = { navigate(ProductDetailsNavigation.GoBack) },
                )
            ),
            isLoading = state.isLoading,
            error = state.error?.message,
            product = state.product,
        )

    override suspend fun handleIntent(
        state: ProductDetailsModelState,
        intent: ProductDetailsIntent,
    ) {
        when (intent) {
            is ProductDetailsIntent.RefreshClicked -> loadData()
        }
    }
}