package com.kakaocafe.order.adapter.out.persistence

import com.kakaocafe.order.adapter.out.persistence.entity.OrderEntity
import com.kakaocafe.order.adapter.out.persistence.jpaRepository.OrderEntityJpaRepository
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.domain.Order
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderPersistenceAdapter(private val orderEntityJpaRepository: OrderEntityJpaRepository) : OrderPersistencePort {
    override fun save(order: Order): Order {
        return orderEntityJpaRepository.save(OrderEntity.of(order)).toDomain()
    }

    override fun findById(id: Long): Order? {
        return orderEntityJpaRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findByIdAndMemberId(orderId: Long, memberId: Long): Order? {
        return orderEntityJpaRepository.findByIdAndMemberId(orderId, memberId)?.toDomain()
    }
}