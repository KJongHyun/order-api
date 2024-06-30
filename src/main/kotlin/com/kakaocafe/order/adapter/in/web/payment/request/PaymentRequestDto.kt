package com.kakaocafe.order.adapter.`in`.web.payment.request

import java.math.BigDecimal

data class PaymentRequestDto(
    val orderId: Long,
    val amount: BigDecimal
)
