package com.kakaocafe.order.application.port.out

import com.kakaocafe.order.domain.Product

interface ProductPersistencePort {
    fun findById(id: Long): Product?
    fun findAllByIds(ids: List<Long>): List<Product>
    fun save(product: Product): Product
}