package com.aromano.superbassignment.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.aromano.superbassignment.presentation.core.Intent
import com.aromano.superbassignment.presentation.core.Navigation
import com.aromano.superbassignment.presentation.core.UiViewModel
import com.aromano.superbassignment.presentation.core.ViewState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent

interface ViewModelComponent<
        ComponentViewState : ViewState,
        ComponentIntent : Intent,
        ComponentNavigation : Navigation,
        > {
    val state: StateFlow<ComponentViewState>
    fun onIntent(intent: ComponentIntent)
}

abstract class DefaultViewModelComponent<
        ComponentViewState : ViewState,
        ComponentIntent : Intent,
        ComponentNavigation : Navigation,
        >(
    componentContext: ComponentContext,
    val navigationHandler: (ComponentNavigation) -> Unit,
) : ViewModelComponent<ComponentViewState, ComponentIntent, ComponentNavigation>,
    ComponentContext by componentContext,
    KoinComponent {

    protected abstract val viewModel: UiViewModel<ComponentViewState, ComponentIntent, ComponentNavigation>

    private val retainedViewModel by lazy {
        instanceKeeper.getOrCreate {
            viewModel.also {
                it.navigationFlow
                    .onEach { event ->
                        event.markAsHandled()
                        navigationHandler(event.navigation)
                    }
                    .launchIn(componentContext.coroutineScope())
            }
        }
    }

    override val state: StateFlow<ComponentViewState> by lazy {
        retainedViewModel.viewStateFlow
    }

    override fun onIntent(intent: ComponentIntent) = retainedViewModel.onIntent(intent)

}