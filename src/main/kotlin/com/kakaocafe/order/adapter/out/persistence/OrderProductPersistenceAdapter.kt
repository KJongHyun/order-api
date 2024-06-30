package com.kakaocafe.order.adapter.out.persistence

import com.kakaocafe.order.adapter.out.persistence.jpaRepository.OrderProductEntityJpaRepository
import org.springframework.stereotype.Repository

@Repository
class OrderProductPersistenceAdapter(private val orderProductEntityJpaRepository: OrderProductEntityJpaRepository) {
}