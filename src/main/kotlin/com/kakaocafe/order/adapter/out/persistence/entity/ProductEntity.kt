package com.kakaocafe.order.adapter.out.persistence.entity

import com.kakaocafe.order.domain.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "product")
class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Long = 0,
    var name: String,
    var price: BigDecimal,
    var stock: Int,
) {
    companion object {
        fun of(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                stock = product.stock
            )
        }
    }

    fun toDomain(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            stock = stock
        )
    }
}