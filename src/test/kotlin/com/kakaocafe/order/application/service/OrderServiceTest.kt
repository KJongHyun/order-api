package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.OrderCommand
import com.kakaocafe.order.application.port.OrderProductInfo
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.Order
import com.kakaocafe.order.domain.OrderProduct
import com.kakaocafe.order.domain.OrderStatus
import com.kakaocafe.order.domain.Product
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderServiceTest {

    private val orderPersistencePort: OrderPersistencePort = mock()
    private val productPersistencePort: ProductPersistencePort = mock()
    private val orderService: OrderService = OrderService(orderPersistencePort, productPersistencePort)

    @Test
    fun `존재하지 않는 productId가 포함된 경우 ORDER_ERROR 예외를 발생시킴`() {
        val orderCommand = OrderCommand(
            memberId = 1,
            orderProducts = listOf(OrderProductInfo(productId = 1, quantity = 1))
        )

        given(productPersistencePort.findAllByIds(any())).willReturn(emptyList())

        val exception = shouldThrow<KakaoCafeException> {
            orderService.order(orderCommand)
        }

        exception.errorCode shouldBe ErrorCode.ORDER_ERROR
    }

    @Test
    fun `재고가 부족한 경우 NOT_ENOUGH_STOCK 예외를 발생시킴`() {
        val product = Product(id = 1, name = "상품1", price = BigDecimal(1000), stock = 0)
        val orderCommand = OrderCommand(
            memberId = 1,
            orderProducts = listOf(OrderProductInfo(productId = 1, quantity = 1))
        )

        given(productPersistencePort.findAllByIds(any())).willReturn(listOf(product))

        val exception = shouldThrow<KakaoCafeException> {
            orderService.order(orderCommand)
        }

        exception.errorCode shouldBe ErrorCode.NOT_ENOUGH_STOCK
    }

    @Test
    fun `정상적인 주문 생성`() {
        val product = Product(id = 1, name = "상품1", price = BigDecimal(1000), stock = 10)
        val orderCommand = OrderCommand(
            memberId = 1,
            orderProducts = listOf(OrderProductInfo(productId = 1, quantity = 5))
        )
        val savedOrder = Order(
            memberId = orderCommand.memberId,
            status = OrderStatus.PENDING_PAYMENT,
            orderProducts = listOf(OrderProduct(productId = 1, price = BigDecimal(1000), quantity = 5)),
            orderedAt = LocalDateTime.now()
        )

        given(productPersistencePort.findAllByIds(orderCommand.orderProducts.map { it.productId })).willReturn(listOf(product))
        given(orderPersistencePort.save(any())).willReturn(savedOrder)


        val result = orderService.order(orderCommand)

        result.memberId shouldBe 1
        result.status shouldBe OrderStatus.PENDING_PAYMENT
    }
}
