package com.kakaocafe.order.adapter.out.persistence.entity

import com.kakaocafe.order.domain.Payment
import com.kakaocafe.order.domain.PaymentStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "payment")
class PaymentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    val id: Long = 0,
    val memberId: Long,
    @Column(name = "cafe_order_id")
    val orderId: Long,
    @Column(name = "payment_uuid")
    val paymentUUID: String,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus,
    val paidAt: LocalDateTime
) {
    companion object {
        fun of(payment: Payment): PaymentEntity {
            return PaymentEntity(
                id = payment.id,
                memberId = payment.memberId,
                orderId = payment.orderId,
                paymentUUID = payment.paymentUUID,
                amount = payment.amount,
                status = payment.status,
                paidAt = payment.paidAt
            )
        }
    }

    fun toDomain(): Payment {
        return Payment(
            id = id,
            memberId = memberId,
            orderId = orderId,
            paymentUUID = paymentUUID,
            amount = amount,
            status = status,
            paidAt = paidAt
        )
    }
}