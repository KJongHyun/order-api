package com.kakaocafe.order.application.port

import java.math.BigDecimal

data class PayCommand(
    val memberId: Long,
    val orderId: Long,
    val amount: BigDecimal
)
