package com.aromano.superbassignment.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aromano.superbassignment.android.ui.core.ComponentScreen
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.presentation.navigation.ProductDetailsComponent
import com.aromano.superbassignment.presentation.productdetails.ProductDetailsIntent
import com.aromano.superbassignment.presentation.utils.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    component: ProductDetailsComponent,
) {
    ComponentScreen(component) { state, onIntent ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { onIntent(ProductDetailsIntent.RefreshClicked) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val error = state.error
                val product = state.product

                if (error != null) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = error
                    )
                } else if (product != null) {
                    Content(product)
                }
            }
        }
    }
}

@Composable
private fun Content(product: Product) {
    Text(
        text = product.name,
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    product.category?.let {
        Text(
            text = "${Strings.product_details_category_label} ${it.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "${Strings.product_details_price_label}${"%.2f".format(product.price.value / 100.0f)}",
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = product.desc ?: Strings.product_details_no_description,
        style = MaterialTheme.typography.bodyLarge
    )
}
