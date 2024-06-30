package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.PayCommand
import com.kakaocafe.order.application.port.`in`.PayUseCase
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.PaymentPersistencePort
import com.kakaocafe.order.application.port.out.PaymentRemotePort
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.OrderStatus
import com.kakaocafe.order.domain.Payment
import com.kakaocafe.order.domain.PaymentStatus
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import com.kakaocafe.order.global.withTimeout
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PayService(
    private val orderPersistencePort: OrderPersistencePort,
    private val paymentRemotePort: PaymentRemotePort,
    private val paymentPersistencePort: PaymentPersistencePort,
    private val productPersistencePort: ProductPersistencePort,
    private val lockService: LockService
) : PayUseCase {

    @Transactional
    override fun pay(payCommand: PayCommand) {
        val order = orderPersistencePort.findByIdAndMemberId(payCommand.orderId, payCommand.memberId)
            ?: throw KakaoCafeException(ErrorCode.DATA_NOT_FOUND)

        if (order.status != OrderStatus.PENDING_PAYMENT) {
            throw KakaoCafeException(ErrorCode.PAY_ERROR, "주문이 결제 불가능한 상태입니다.")
        }

        if (order.totalPrice() != payCommand.amount) {
            throw KakaoCafeException(ErrorCode.PAY_ERROR, "주문 금액과 결제 금액이 다릅니다.")
        }

        val orderLockKey = "pay:${payCommand.orderId}"
        val productLockKeys = order.orderProducts.map {
            "productId:${it.productId}"
        }

        lockService.lockScope(keys = productLockKeys + orderLockKey) {
            val paymentUUID = withTimeout(8) {
                 paymentRemotePort.makePayment()
            }

            runCatching {
                order.orderProducts.forEach { orderProduct ->
                    val product = productPersistencePort.findById(orderProduct.productId) ?: throw KakaoCafeException(ErrorCode.DATA_NOT_FOUND)
                    product.reduceStockForOrder(orderProduct.quantity)
                    productPersistencePort.save(product)
                }
                order.status = OrderStatus.COMPLETED
                orderPersistencePort.save(order)
                paymentPersistencePort.save(
                    Payment(
                        memberId = payCommand.memberId,
                        orderId = order.id,
                        paymentUUID = paymentUUID,
                        amount = order.totalPrice(),
                        status = PaymentStatus.PAID,
                        paidAt = LocalDateTime.now()
                    )
                )
            }.onFailure {
                paymentRemotePort.cancelPayment(paymentUUID)
            }.getOrThrow()
        }

    }
}