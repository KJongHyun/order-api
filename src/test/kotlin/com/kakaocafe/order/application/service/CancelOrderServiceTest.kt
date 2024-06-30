package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.CancelOrderCommand
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.PaymentPersistencePort
import com.kakaocafe.order.application.port.out.PaymentRemotePort
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.*
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.math.BigDecimal

class CancelOrderServiceTest {

    private val orderPersistencePort: OrderPersistencePort = mock()
    private val productPersistencePort: ProductPersistencePort = mock()
    private val paymentRemotePort: PaymentRemotePort = mock()
    private val paymentPersistencePort: PaymentPersistencePort = mock()
    private val lockService: LockService = mock()
    private val cancelOrderService: CancelOrderService = CancelOrderService(
        orderPersistencePort,
        productPersistencePort,
        paymentRemotePort,
        paymentPersistencePort,
        lockService
    )

    @Test
    fun `주문이 존재하지 않으면 NOT_EXIST_ORDER 예외를 발생시킴`() {
        given(orderPersistencePort.findByIdAndMemberId(any(), any())).willReturn(null)

        val command = CancelOrderCommand(memberId = 1, orderId = 1)

        val exception = shouldThrow<KakaoCafeException> {
            cancelOrderService.cancel(command)
        }

        exception.errorCode shouldBe ErrorCode.NOT_EXIST_ORDER
    }

    @Test
    fun `주문이 취소 불가능한 상태인 경우 CANNOT_CANCEL_ORDER 예외를 발생시킴`() {
        val order = mock<Order>()
        given(orderPersistencePort.findByIdAndMemberId(any(), any())).willReturn(order)
        given(order.status).willReturn(OrderStatus.CANCELLED)

        val command = CancelOrderCommand(memberId = 1, orderId = 1)

        val exception = shouldThrow<KakaoCafeException> {
            cancelOrderService.cancel(command)
        }

        exception.errorCode shouldBe  ErrorCode.CANNOT_CANCEL_ORDER
    }

    @Test
    fun `결제가 존재하지 않으면 NOT_EXIST_PAYMENT 예외를 발생시킴`() {
        val order = mock<Order>()
        given(orderPersistencePort.findByIdAndMemberId(any(), any())).willReturn(order)
        given(order.status).willReturn(OrderStatus.COMPLETED)
        given(paymentPersistencePort.findByMemberIdAndOrderId(any(), any())).willReturn(null)

        val command = CancelOrderCommand(memberId = 1, orderId = 1)

        val exception = shouldThrow<KakaoCafeException> {
            cancelOrderService.cancel(command)
        }

        exception.errorCode shouldBe  ErrorCode.NOT_EXIST_PAYMENT
    }

    @Test
    fun `정상적인 취소 작업을 수행함`() {
        val order = mock<Order>()
        val payment = mock<Payment>()
        val product = mock<Product>()

        given(order.status).willReturn(OrderStatus.COMPLETED)
        given(order.orderProducts).willReturn(
            listOf(
                OrderProduct(
                    productId = 1,
                    quantity = 1,
                    price = BigDecimal(3000)
                )
            )
        )
        given(orderPersistencePort.findByIdAndMemberId(any(), any())).willReturn(order)

        given(payment.paymentUUID).willReturn("SAMPLE-UUID")
        given(paymentPersistencePort.findByMemberIdAndOrderId(any(), any())).willReturn(payment)
        given(productPersistencePort.findById(any())).willReturn(product)

        given(paymentRemotePort.cancelPayment(any())).willReturn("Success")

        doAnswer { invocation ->
            val block = invocation.getArgument<() -> Unit>(2)
            block.invoke()
            null
        }.whenever(lockService).lockScope<Any>(any<List<String>>(), any(), any())

        cancelOrderService.cancel(CancelOrderCommand(memberId = 1, orderId = 1))

        verify(orderPersistencePort).save(any<Order>())
        verify(paymentPersistencePort).save(any<Payment>())
        verify(productPersistencePort).save(any<Product>())
        verify(paymentRemotePort).cancelPayment(any())
        verify(lockService).lockScope<Any>(keys = any(), lockTimeoutSeconds = any(), block = any())
    }
}