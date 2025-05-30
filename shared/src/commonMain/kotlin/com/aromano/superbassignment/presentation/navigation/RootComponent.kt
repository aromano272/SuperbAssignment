package com.aromano.superbassignment.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.presentation.core.UiViewModel
import com.aromano.superbassignment.presentation.di.getViewModel
import com.aromano.superbassignment.presentation.navigation.DefaultRootComponent.Config
import com.aromano.superbassignment.presentation.pos.PosArgs
import com.aromano.superbassignment.presentation.pos.PosContract
import com.aromano.superbassignment.presentation.pos.PosIntent
import com.aromano.superbassignment.presentation.pos.PosNavigation
import com.aromano.superbassignment.presentation.pos.PosViewState
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsArgs
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsContract
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsIntent
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsNavigation
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsViewState
import kotlinx.serialization.Serializable

interface RootComponent : BackHandlerOwner {
    val childStack: Value<ChildStack<*, Child>>

    fun onBackClicked()
    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class Pos(val component: PosComponent) : Child()
        class ProductDetails(val component: ProductDetailsComponent) : Child()
    }

}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Pos,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child {
        return when (config) {
            is Config.Pos -> {
                RootComponent.Child.Pos(
                    DefaultPosComponent(context, config) { navEvent ->
                        when (navEvent) {
                            is PosNavigation.GoToDetails ->
                                navigation.pushNew(Config.ProductDetails(navEvent.id))
                        }
                    }
                )
            }

            is Config.ProductDetails -> RootComponent.Child.ProductDetails(
                DefaultProductDetailsComponent(context, config) { navEvent ->
                    when (navEvent) {
                        is ProductDetailsNavigation.GoBack ->
                            navigation.pop()
                    }
                }
            )
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Pos : Config()

        @Serializable
        data class ProductDetails(val id: ProductId) : Config()
    }

}

interface PosComponent : ViewModelComponent<PosViewState, PosIntent, PosNavigation>

class DefaultPosComponent(
    componentContext: ComponentContext,
    config: Config.Pos,
    navigationHandler: (PosNavigation) -> Unit,
) : DefaultViewModelComponent<PosViewState, PosIntent, PosNavigation>(
    componentContext,
    navigationHandler
),
    PosComponent {

    override val viewModel: UiViewModel<PosViewState, PosIntent, PosNavigation> =
        getViewModel<PosArgs, PosContract>(PosArgs)

}

interface ProductDetailsComponent :
    ViewModelComponent<ProductDetailsViewState, ProductDetailsIntent, ProductDetailsNavigation>

class DefaultProductDetailsComponent(
    componentContext: ComponentContext,
    config: Config.ProductDetails,
    navigationHandler: (ProductDetailsNavigation) -> Unit,
) : DefaultViewModelComponent<ProductDetailsViewState, ProductDetailsIntent, ProductDetailsNavigation>(
    componentContext,
    navigationHandler
),
    ProductDetailsComponent {

    override val viewModel: UiViewModel<ProductDetailsViewState, ProductDetailsIntent, ProductDetailsNavigation> =
        getViewModel<ProductDetailsArgs, ProductDetailsContract>(ProductDetailsArgs(config.id))

}
