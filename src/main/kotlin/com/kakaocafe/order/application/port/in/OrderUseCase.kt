package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.OrderCommand
import com.kakaocafe.order.domain.Order

interface OrderUseCase {
    fun order(orderCommand: OrderCommand): Order
}