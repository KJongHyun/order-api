package com.kakaocafe.order.application.port

data class CancelOrderCommand(
    val memberId: Long,
    val orderId: Long,
)
