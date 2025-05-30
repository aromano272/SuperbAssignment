package com.aromano.superbassignment.presentation.utils

import com.aromano.superbassignment.domain.model.ProductSortStrategy

val ProductSortStrategy.label: String
    get() = when (this) {
        ProductSortStrategy.NAME_ASC -> Strings.sort_strategy_name_asc_label
        ProductSortStrategy.PRICE_ASC -> Strings.sort_strategy_price_asc_label
    }