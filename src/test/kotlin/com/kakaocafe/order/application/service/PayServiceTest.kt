package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.PayCommand
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.PaymentPersistencePort
import com.kakaocafe.order.application.port.out.PaymentRemotePort
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
import org.mockito.kotlin.*
import java.math.BigDecimal

class PayServiceTest {

    private val orderPersistencePort: OrderPersistencePort = mock()
    private val paymentRemotePort: PaymentRemotePort = mock()
    private val paymentPersistencePort: PaymentPersistencePort = mock()
    private val productPersistencePort: ProductPersistencePort = mock()
    private val lockService: LockService = mock()
    private val payService: PayService = PayService(
        orderPersistencePort,
        paymentRemotePort,
        paymentPersistencePort,
        productPersistencePort,
        lockService
    )

    @Test
    fun `존재하지 않는 주문이면 DATA_NOT_FOUND 예외를 발생시킴`() {
        val payCommand = PayCommand(orderId = 1, memberId = 1, amount = BigDecimal(1000))

        given(orderPersistencePort.findByIdAndMemberId(payCommand.orderId, payCommand.memberId)).willReturn(null)

        val exception = shouldThrow<KakaoCafeException> {
            payService.pay(payCommand)
        }

        exception.errorCode shouldBe ErrorCode.DATA_NOT_FOUND
    }

    @Test
    fun `결제 상태가 PENDING_PAYMENT가 아닌 경우 PAY_ERROR 예외를 발생시킴`() {
        val order = mock<Order> {
            on { status } doReturn OrderStatus.COMPLETED
        }
        val payCommand = PayCommand(orderId = 1, memberId = 1, amount = BigDecimal(1000))

        given(orderPersistencePort.findByIdAndMemberId(payCommand.orderId, payCommand.memberId)).willReturn(order)

        val exception = shouldThrow<KakaoCafeException> {
            payService.pay(payCommand)
        }

        exception.errorCode shouldBe ErrorCode.PAY_ERROR
        exception.message shouldBe "주문이 결제 불가능한 상태입니다."
    }

    @Test
    fun `결제 금액과 주문 금액이 다른 경우 PAY_ERROR 예외를 발생시킴`() {
        val order = mock<Order> {
            on { status } doReturn OrderStatus.PENDING_PAYMENT
            on { totalPrice() } doReturn BigDecimal(2000)
        }
        val payCommand = PayCommand(orderId = 1, memberId = 1, amount = BigDecimal(1000))

        given(orderPersistencePort.findByIdAndMemberId(payCommand.orderId, payCommand.memberId)).willReturn(order)

        val exception = shouldThrow<KakaoCafeException> {
            payService.pay(payCommand)
        }

        exception.errorCode shouldBe ErrorCode.PAY_ERROR
        exception.message shouldBe "주문 금액과 결제 금액이 다릅니다."
    }

    @Test
    fun `정상적인 결제 처리`() {
        val product = mock<Product> {
            on { id } doReturn 1
            on { stock } doReturn 10
        }
        val orderProduct = OrderProduct(productId = 1, price = BigDecimal(1000), quantity = 1)
        val order = mock<Order> {
            on { id } doReturn 1
            on { status } doReturn OrderStatus.PENDING_PAYMENT
            on { orderProducts } doReturn listOf(orderProduct)
            on { totalPrice() } doReturn BigDecimal(1000)
        }
        val payCommand = PayCommand(orderId = 1, memberId = 1, amount = BigDecimal(1000))
        val paymentUUID = "paymentUUID"

        given(orderPersistencePort.findByIdAndMemberId(payCommand.orderId, payCommand.memberId)).willReturn(order)
        doAnswer { invocation ->
            val block = invocation.getArgument<() -> Unit>(2)
            block.invoke()
            null
        }.whenever(lockService).lockScope<Any>(keys = any(), any(), any())
        given(paymentRemotePort.makePayment()).willReturn(paymentUUID)
        given(productPersistencePort.findById(1)).willReturn(product)

        payService.pay(payCommand)

        verify(lockService).lockScope<Any>(any<List<String>>(), any(), any())
        verify(product).reduceStockForOrder(1)
        verify(productPersistencePort).save(product)
        verify(orderPersistencePort).save(order)
    }
}
