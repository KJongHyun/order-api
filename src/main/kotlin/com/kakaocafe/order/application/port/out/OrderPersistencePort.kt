package com.kakaocafe.order.application.port.out

import com.kakaocafe.order.domain.Order

interface OrderPersistencePort {
    fun save(order: Order): Order
    fun findById(id: Long): Order?
    fun findByIdAndMemberId(orderId: Long, memberId: Long): Order?
}