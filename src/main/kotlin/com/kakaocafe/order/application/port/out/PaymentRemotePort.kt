package com.kakaocafe.order.application.port.out

interface PaymentRemotePort {
    fun makePayment(): String
    fun cancelPayment(paymentUUID: String): String
}