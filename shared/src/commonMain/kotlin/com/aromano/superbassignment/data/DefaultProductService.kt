package com.aromano.superbassignment.data

import com.aromano.superbassignment.domain.ProductService
import com.aromano.superbassignment.domain.core.Outcome
import com.aromano.superbassignment.domain.core.mapSuccess
import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.domain.model.ProductSortStrategy
import com.aromano.superbassignment.remote.ProductApi
import com.aromano.superbassignment.remote.model.toDomain

class DefaultProductService(
    private val productApi: ProductApi,
) : ProductService {

    override suspend fun getProducts(
        sort: ProductSortStrategy,
        filterByPriceLowerThan: Cents?,
    ): Outcome<List<Product>> =
        productApi.getProducts(sort, filterByPriceLowerThan)
            .mapSuccess { products ->
                products.map { it.toDomain() }
            }

    override suspend fun getProductById(id: ProductId): Outcome<Product> =
        productApi.getProductById(id)
            .mapSuccess { it.toDomain() }

}