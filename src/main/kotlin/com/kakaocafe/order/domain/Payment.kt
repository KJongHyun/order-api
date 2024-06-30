package com.kakaocafe.order.domain

import java.math.BigDecimal
import java.time.LocalDateTime

class Payment(
    val id: Long = 0,
    val memberId: Long,
    val orderId: Long,
    val paymentUUID: String,
    val amount: BigDecimal,
    var status: PaymentStatus,
    val paidAt: LocalDateTime,
    var cancelledAt: LocalDateTime? = null
) {
    fun cancel() {
        status = PaymentStatus.CANCELLED
        cancelledAt = LocalDateTime.now()
    }
}

enum class PaymentStatus(val description: String) {
    PAID("결제 완료"), CANCELLED("결제 취소")
}