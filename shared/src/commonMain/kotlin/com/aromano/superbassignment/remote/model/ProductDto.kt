package com.aromano.superbassignment.remote.model

import com.aromano.superbassignment.domain.model.Cents
import com.aromano.superbassignment.domain.model.Product
import com.aromano.superbassignment.domain.model.ProductId
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: ProductId,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val category: ProductCategoryDto?,
)

fun ProductDto.toDomain() = Product(
    id = id,
    name = name,
    desc = description,
    price = Cents(priceCents),
    category = category?.toDomain(),
)