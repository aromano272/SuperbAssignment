package com.aromano.superbassignment.presentation.core

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UiViewModel<
        TViewState : ViewState,
        TIntent : Intent,
        TNavigation : Navigation,
        > : InstanceKeeper.Instance {

    val viewStateFlow: StateFlow<TViewState>

    val navigationFlow: Flow<NavigationEvent<TNavigation>>

    fun onIntent(intent: TIntent)

}