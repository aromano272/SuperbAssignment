package com.aromano.superbassignment.remote

import com.aromano.superbassignment.domain.core.ErrorKt
import com.aromano.superbassignment.domain.core.Outcome
import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.ProductId
import com.aromano.superbassignment.domain.model.ProductSortStrategy
import com.aromano.superbassignment.remote.model.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface ProductApi {

    suspend fun getProducts(
        sort: ProductSortStrategy,
        filterByPriceLowerThan: Cents?,
    ): Outcome<List<ProductDto>>

    suspend fun getProductById(id: ProductId): Outcome<ProductDto>

}

class DefaultProductApi : ProductApi {

    override suspend fun getProducts(
        sort: ProductSortStrategy,
        filterByPriceLowerThan: Cents?,
    ): Outcome<List<ProductDto>> = withContext(Dispatchers.Default) {
        delay(500) // Simulates network latency

        val data = if (filterByPriceLowerThan != null) {
            STUB_PRODUCTS.filter { it.priceCents < filterByPriceLowerThan.value }
        } else {
            STUB_PRODUCTS
        }.sortedWith(
            compareBy { product ->
                when (sort) {
                    ProductSortStrategy.NAME_ASC -> product.name
                    ProductSortStrategy.PRICE_ASC -> product.priceCents
                }
            }
        )

        Outcome.Success(data)
    }

    override suspend fun getProductById(
        id: ProductId,
    ): Outcome<ProductDto> = withContext(Dispatchers.Default) {
        delay(500)
        STUB_PRODUCTS.find { it.id == id }
            ?.let { Outcome.Success(it) }
            ?: Outcome.Failure(ErrorKt.NotFound)
    }

}
