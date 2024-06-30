package com.kakaocafe.order.adapter.`in`.web.order.request

data class OrderRequestDto(
    val orderProducts: List<OrderProductInfoRequestDto>
)

data class OrderProductInfoRequestDto(
    val productId: Long,
    val quantity: Int
)
