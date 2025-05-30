package com.aromano.superbassignment.remote.model

import com.aromano.superbassignment.domain.model.ProductCategory
import com.aromano.superbassignment.domain.model.ProductCategoryId
import kotlinx.serialization.Serializable

@Serializable
data class ProductCategoryDto(
    val id: ProductCategoryId,
    val name: String,
)

fun ProductCategoryDto.toDomain() = ProductCategory(
    id = id,
    name = name,
)