package com.aromano.superbassignment.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aromano.superbassignment.android.ui.core.ComponentScreen
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.presentation.navigation.PosComponent
import com.aromano.superbassignment.presentation.pos.PosIntent
import com.aromano.superbassignment.presentation.pos.PosViewState
import com.aromano.superbassignment.presentation.utils.Strings
import com.aromano.superbassignment.presentation.utils.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    component: PosComponent,
) {
    ComponentScreen(component) { state, onIntent ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { onIntent(PosIntent.RefreshClicked) }
        ) {
            Column {
                FilterRow(state, onIntent)

                Content(state, onIntent)
            }
        }
    }
}

@Composable
private fun FilterRow(
    state: PosViewState,
    onIntent: (PosIntent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            var pickerOpen by remember { mutableStateOf(false) }

            FilterChip(
                selected = false,
                leadingIcon = { Icon(Icons.Default.FilterAlt, null) },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                onClick = { pickerOpen = true },
                label = { Text(state.selectedSortStrategy.label) },
            )

            DropdownMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                expanded = pickerOpen,
                onDismissRequest = { pickerOpen = false }
            ) {
                state.availableSortStrategies.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.label) },
                        onClick = {
                            onIntent(PosIntent.SortStrategyChanged(option))
                            pickerOpen = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(Strings.pos_price_filter_label)

            OutlinedTextField(
                modifier = Modifier.width(100.dp),
                value = state.priceFilterInput,
                onValueChange = { onIntent(PosIntent.PriceFilterInputChanged(it)) },
                singleLine = true,
                isError = state.priceFilterInputError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                ),
            )
        }
    }
}

@Composable
private fun ColumnScope.Content(
    state: PosViewState,
    onIntent: (PosIntent) -> Unit,
) {
    val error = state.error
    if (error != null) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = error
        )
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(state.products.orEmpty()) { product ->
                ProductItem(product) {
                    onIntent(PosIntent.ProductClicked(product.id))
                }
            }
        }
    }
}

@Composable
private fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                product.category?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "$${"%.2f".format(product.price.value / 100.0f)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}