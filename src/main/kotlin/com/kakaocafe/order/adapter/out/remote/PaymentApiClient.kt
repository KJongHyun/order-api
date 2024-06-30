package com.kakaocafe.order.adapter.out.remote

import com.kakaocafe.order.application.port.out.PaymentRemotePort
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

@Component
class PaymentApiClient : PaymentRemotePort {

    @Retryable
    override fun makePayment(): String {
        Thread.sleep(abs(Random.nextLong(10)))

        if(Random.nextInt() % 100 == 1) {
            throw Exception("Failed!");
        }

        return UUID.randomUUID().toString()
    }

    @Retryable
    override fun cancelPayment(paymentUUID: String): String {
        Thread.sleep(abs(Random.nextLong(10)))

        if(Random.nextInt() % 100 == 1) {
            throw Exception("Failed!")
        }

        return paymentUUID
    }

}