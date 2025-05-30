package com.aromano.superbassignment.presentation.di

import com.aromano.superbassignment.presentation.core.Args
import com.aromano.superbassignment.presentation.core.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

inline fun <
        reified TArgs : Args,
        reified VM : ViewModel<TArgs, *, *, *, *>,
        > Module.viewModelFactory(
    qualifier: Qualifier? = null,
    noinline definition: Scope.(TArgs) -> VM,
): KoinDefinition<VM> = factory<VM>(qualifier) { (args: TArgs) ->
    definition(args)
}

inline fun <
        reified TArgs : Args,
        reified VM : ViewModel<TArgs, *, *, *, *>,
        > KoinComponent.getViewModel(
    args: TArgs,
    qualifier: Qualifier? = null,
): VM = get(
    qualifier = qualifier,
    parameters = { parametersOf(args) },
)

