package com.kakaocafe.order.domain

import java.math.BigDecimal

class OrderProduct(
    val id: Long = 0,
    val productId: Long,
    val price: BigDecimal,
    val quantity: Int,
)