package com.kakaocafe.order.adapter.out.persistence.entity

import com.kakaocafe.order.domain.Order
import com.kakaocafe.order.domain.OrderStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "cafe_order")
class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_order_id")
    val id: Long = 0,
    val memberId: Long,
    var orderedAt: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "cafe_order_id")
    val orderProducts: List<OrderProductEntity> = listOf(),
    val totalPrice: BigDecimal
) {
    companion object {
        fun of(order: Order): OrderEntity {
            return OrderEntity(
                id = order.id,
                memberId = order.memberId,
                orderedAt = order.orderedAt,
                status = order.status,
                orderProducts = order.orderProducts.map { OrderProductEntity.of(it) },
                totalPrice = order.totalPrice()
            )
        }
    }

    fun toDomain(): Order {
        return Order(
            id = id,
            memberId = memberId,
            orderedAt = orderedAt,
            status = status,
            orderProducts = orderProducts.map {
                it.toDomain()
            }
        )
    }
}