package com.aromano.superbassignment.domain

import com.aromano.superbassignment.domain.core.Outcome
import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.domain.model.ProductSortStrategy

interface ProductService {

    suspend fun getProducts(
        sort: ProductSortStrategy,
        filterByPriceLowerThan: Cents?,
    ): Outcome<List<Product>>

    suspend fun getProductById(id: ProductId): Outcome<Product>

}