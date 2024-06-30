package com.kakaocafe.order.adapter.out.persistence.jpaRepository

import com.kakaocafe.order.adapter.out.persistence.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEntityJpaRepository : JpaRepository<PaymentEntity, Long> {
    fun findByOrderId(orderId: Long): PaymentEntity?
}