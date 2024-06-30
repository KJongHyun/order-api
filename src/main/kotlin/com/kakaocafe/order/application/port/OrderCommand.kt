package com.kakaocafe.order.application.port

data class OrderCommand(
    val memberId: Long,
    val orderProducts: List<OrderProductInfo>
)

data class OrderProductInfo(
    val productId: Long,
    val quantity: Int
)
