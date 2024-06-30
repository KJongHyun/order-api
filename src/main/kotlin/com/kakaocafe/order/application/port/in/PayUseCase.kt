package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.PayCommand

interface PayUseCase {
    fun pay(payCommand: PayCommand)
}