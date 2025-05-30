package com.aromano.superbassignment.android.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aromano.superbassignment.presentation.core.Intent
import com.aromano.superbassignment.presentation.core.Navigation
import com.aromano.superbassignment.presentation.core.ViewState
import com.aromano.superbassignment.presentation.core.ViewStateWithCommonState
import com.aromano.superbassignment.presentation.navigation.ViewModelComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <
        TIntent : Intent,
        TViewState : ViewState,
        TNavigation : Navigation,
        VM : ViewModelComponent<TViewState, TIntent, TNavigation>,
        > ComponentScreen(
    viewModel: VM,
    content: @Composable (state: TViewState, onIntent: (TIntent) -> Unit) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            (viewState as? ViewStateWithCommonState)?.commonState?.let { state ->
                state.topBarViewState?.let { topBar ->
                    TopAppBar(
                        title = { Text(topBar.title) },
                        navigationIcon = {
                            if (topBar.onBackHandler != null) {
                                IconButton(onClick = {
                                    topBar.onBackHandler?.invoke()
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                    )
                }
            }

            content(viewState, viewModel::onIntent)
        }

        (viewState as? ViewStateWithCommonState)?.commonState?.let { state ->
            (state.errorAlert ?: state.successAlert)?.let {
                LocalSoftwareKeyboardController.current?.hide()
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    snackbarData = CustomSnackbarData(it),
                )
            }
        }
    }
}

data class CustomSnackbarData(
    override val message: String,
) : SnackbarData, SnackbarVisuals {
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite

    override val visuals: SnackbarVisuals = this

    override fun dismiss() {
    }

    override fun performAction() {
    }

    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
}