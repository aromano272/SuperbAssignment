package com.aromano.superbassignment.domain.model

typealias ProductId = Int

data class Product(
    val id: ProductId,
    val name: String,
    val desc: String?,
    val price: Cents,
    val category: ProductCategory?,
)