package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.CancelOrderCommand
import com.kakaocafe.order.application.port.`in`.CancelOrderUseCase
import com.kakaocafe.order.application.port.out.OrderPersistencePort
import com.kakaocafe.order.application.port.out.PaymentPersistencePort
import com.kakaocafe.order.application.port.out.PaymentRemotePort
import com.kakaocafe.order.application.port.out.ProductPersistencePort
import com.kakaocafe.order.domain.OrderStatus
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import com.kakaocafe.order.global.withTimeout
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CancelOrderService(
    private val orderPersistencePort: OrderPersistencePort,
    private val productPersistencePort: ProductPersistencePort,
    private val paymentRemotePort: PaymentRemotePort,
    private val paymentPersistencePort: PaymentPersistencePort,
    private val lockService: LockService,
) : CancelOrderUseCase {

    @Transactional
    override fun cancel(cancelOrderCommand: CancelOrderCommand) {
        val order = orderPersistencePort.findByIdAndMemberId(cancelOrderCommand.memberId, cancelOrderCommand.orderId)
            ?: throw KakaoCafeException(ErrorCode.NOT_EXIST_ORDER)

        if (order.status != OrderStatus.COMPLETED) {
            throw KakaoCafeException(ErrorCode.CANNOT_CANCEL_ORDER)
        }

        val payment = paymentPersistencePort.findByMemberIdAndOrderId(cancelOrderCommand.memberId, order.id) ?: throw KakaoCafeException(ErrorCode.NOT_EXIST_PAYMENT)

        withTimeout(8) {
            paymentRemotePort.cancelPayment(payment.paymentUUID)
        }

        val productLockKeys = order.orderProducts.map {
            "productId:${it.productId}"
        }

        lockService.lockScope(productLockKeys) {
            order.orderProducts.map {
                val product =
                    productPersistencePort.findById(it.productId) ?: throw KakaoCafeException(ErrorCode.DATA_NOT_FOUND)
                product.restoreStock(it.quantity)
                productPersistencePort.save(product)
            }

            payment.cancel()
            paymentPersistencePort.save(payment)

            order.cancel()
            orderPersistencePort.save(order)
        }
    }
}