package com.kakaocafe.order.adapter.out.persistence.jpaRepository

import com.kakaocafe.order.adapter.out.persistence.entity.OrderProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderProductEntityJpaRepository : JpaRepository<OrderProductEntity, Long> {
}