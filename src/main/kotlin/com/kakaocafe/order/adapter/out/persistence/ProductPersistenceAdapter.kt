package com.kakaocafe.order.adapter.out.persistence

import com.kakaocafe.order.adapter.out.persistence.entity.ProductEntity
import com.kakaocafe.order.adapter.out.persistence.jpaRepository.ProductEntityJpaRepository
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.Product
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductPersistenceAdapter(private val productEntityJpaRepository: ProductEntityJpaRepository) : ProductPersistencePort {
    override fun findById(id: Long): Product? {
        return productEntityJpaRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findAllByIds(ids: List<Long>): List<Product> {
        return productEntityJpaRepository.findAllByIdIn(ids)
    }

    override fun save(product: Product): Product {
        return productEntityJpaRepository.save(ProductEntity.of(product)).toDomain()
    }
}