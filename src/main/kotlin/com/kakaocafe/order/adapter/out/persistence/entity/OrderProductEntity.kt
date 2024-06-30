package com.kakaocafe.order.adapter.out.persistence.entity

import com.kakaocafe.order.domain.OrderProduct
import com.kakaocafe.order.domain.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "cafe_order_product")
class OrderProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_order_product_id")
    val id: Long = 0,
    val productId: Long,
    val price: BigDecimal,
    val quantity: Int,
) {
    companion object {
        fun of(orderProduct: OrderProduct): OrderProductEntity {
            return OrderProductEntity(
                id = orderProduct.id,
                productId = orderProduct.productId,
                price = orderProduct.price,
                quantity = orderProduct.quantity,
            )
        }
    }

    fun toDomain(): OrderProduct {
        return OrderProduct(
            id = id,
            productId = productId,
            price = price,
            quantity = quantity,
        )
    }
}