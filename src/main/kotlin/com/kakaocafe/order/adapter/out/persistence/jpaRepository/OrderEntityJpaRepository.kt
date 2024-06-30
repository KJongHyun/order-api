package com.kakaocafe.order.adapter.out.persistence.jpaRepository

import com.kakaocafe.order.adapter.out.persistence.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderEntityJpaRepository : JpaRepository<OrderEntity, Long> {
    fun findByIdAndMemberId(orderId: Long, memberId: Long): OrderEntity?
}