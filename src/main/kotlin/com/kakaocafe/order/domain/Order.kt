package com.kakaocafe.order.domain

import java.math.BigDecimal
import java.time.LocalDateTime

class Order(
    val id: Long = 0,
    val memberId: Long,
    var status: OrderStatus,
    val orderProducts: List<OrderProduct>,
    var orderedAt: LocalDateTime,
) {
    fun totalPrice() = orderProducts.sumOf { it.price * BigDecimal(it.quantity) }

    fun cancel() {
        status = OrderStatus.CANCELLED
    }
}

enum class OrderStatus(val description: String) {
    PENDING_PAYMENT("결제 대기"),
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소")
}