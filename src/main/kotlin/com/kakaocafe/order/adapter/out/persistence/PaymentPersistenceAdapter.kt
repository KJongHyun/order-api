package com.kakaocafe.order.adapter.out.persistence

import com.kakaocafe.order.adapter.out.persistence.entity.PaymentEntity
import com.kakaocafe.order.adapter.out.persistence.jpaRepository.PaymentEntityJpaRepository
import com.kakaocafe.order.application.port.out.PaymentPersistencePort
import com.kakaocafe.order.domain.Payment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PaymentPersistenceAdapter(private val paymentEntityJpaRepository: PaymentEntityJpaRepository) : PaymentPersistencePort {
    override fun save(payment: Payment): Payment {
        return paymentEntityJpaRepository.save(PaymentEntity.of(payment)).toDomain()
    }

    override fun findById(payment: Payment): Payment? {
        return paymentEntityJpaRepository.findByIdOrNull(payment.id)?.toDomain()
    }

    override fun findByMemberIdAndOrderId(memberId: Long, orderId: Long): Payment? {
        return paymentEntityJpaRepository.findByOrderId(orderId)?.toDomain()
    }
}