package com.aromano.superbassignment.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.aromano.superbassignment.presentation.navigation.RootComponent

@Composable
fun RootScreen(component: RootComponent) {
    Box(Modifier.fillMaxSize()) {
        Children(component.childStack) {
            when (val child = it.instance) {
                is RootComponent.Child.Pos -> PosScreen(child.component)
                is RootComponent.Child.ProductDetails -> ProductDetailsScreen(child.component)
            }
        }
    }
}