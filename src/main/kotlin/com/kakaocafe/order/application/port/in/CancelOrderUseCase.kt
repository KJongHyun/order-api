package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.CancelOrderCommand

interface CancelOrderUseCase {
    fun cancel(cancelOrderCommand: CancelOrderCommand)
}