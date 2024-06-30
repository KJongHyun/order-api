package com.kakaocafe.order.application.port.out

import com.kakaocafe.order.domain.Payment

interface PaymentPersistencePort {
    fun save(payment: Payment): Payment
    fun findById(payment: Payment): Payment?
    fun findByMemberIdAndOrderId(memberId: Long, orderId: Long): Payment?
}