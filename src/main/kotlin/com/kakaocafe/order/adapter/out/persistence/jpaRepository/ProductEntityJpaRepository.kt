package com.kakaocafe.order.adapter.out.persistence.jpaRepository

import com.kakaocafe.order.adapter.out.persistence.entity.ProductEntity
import com.kakaocafe.order.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEntityJpaRepository : JpaRepository<ProductEntity, Long> {
    fun findAllByIdIn(productIds: List<Long>): List<Product>
}