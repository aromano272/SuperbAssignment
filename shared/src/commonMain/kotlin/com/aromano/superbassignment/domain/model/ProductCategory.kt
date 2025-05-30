package com.aromano.superbassignment.domain.model

typealias ProductCategoryId = Int

data class ProductCategory(
    val id: ProductCategoryId,
    val name: String,
)