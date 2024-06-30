package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.OrderCommand
import com.kakaocafe.order.application.port.`in`.OrderUseCase
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.Order
import com.kakaocafe.order.domain.OrderProduct
import com.kakaocafe.order.domain.OrderStatus
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderPersistencePort: OrderPersistencePort,
    private val productPersistencePort: ProductPersistencePort
) : OrderUseCase {

    override fun order(orderCommand: OrderCommand): Order {
        val productIds = orderCommand.orderProducts.map { it.productId }
        val products = productPersistencePort.findAllByIds(productIds).associateBy { it.id }

        val invalidProductIds = productIds.filterNot { it in products.keys }

        if (invalidProductIds.isNotEmpty()) {
            throw KakaoCafeException(ErrorCode.ORDER_ERROR, "존재 하지 않는 productId : $invalidProductIds")
        }

        val orderProducts = orderCommand.orderProducts.map { orderProductInfo ->
            val product = products[orderProductInfo.productId] ?: throw KakaoCafeException(ErrorCode.ORDER_ERROR, "존재 하지 않는 productId")

            if (product.stock < orderProductInfo.quantity) {
                throw KakaoCafeException(ErrorCode.NOT_ENOUGH_STOCK, "재고가 부족한 상품 : ${orderProductInfo.productId}")
            }

            OrderProduct(
                price = product.price,
                productId = orderProductInfo.productId,
                quantity = orderProductInfo.quantity
            )
        }

        return orderPersistencePort.save(
            Order(
                memberId = orderCommand.memberId,
                status = OrderStatus.PENDING_PAYMENT,
                orderProducts = orderProducts,
                orderedAt = LocalDateTime.now()
            )
        )
    }
}