package com.kakaocafe.order.adapter.`in`.web.order

import com.kakaocafe.order.adapter.`in`.web.member.LoginMember
import com.kakaocafe.order.adapter.`in`.web.member.LoginMemberInfo
import com.kakaocafe.order.adapter.`in`.web.order.request.OrderRequestDto
import com.kakaocafe.order.adapter.`in`.web.order.response.OrderResponse
import com.kakaocafe.order.application.port.CancelOrderCommand
import com.kakaocafe.order.application.port.OrderCommand
import com.kakaocafe.order.application.port.OrderProductInfo
import com.kakaocafe.order.application.port.`in`.CancelOrderUseCase
import com.kakaocafe.order.application.port.`in`.OrderUseCase
import com.kakaocafe.order.domain.MemberStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders")
class OrderController(
    private val orderUseCase: OrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
) {
    @PostMapping
    fun order(
        @LoginMember([MemberStatus.OK]) loginMemberInfo: LoginMemberInfo,
        @RequestBody orderRequestDto: OrderRequestDto
    ): OrderResponse {
        val order = orderUseCase.order(
            OrderCommand(loginMemberInfo.memberId, orderRequestDto.orderProducts.map {
                OrderProductInfo(it.productId, it.quantity)
            })
        )

        return OrderResponse(order.id)
    }

    @DeleteMapping("/{orderId}")
    fun cancelOrder(
        @LoginMember([MemberStatus.OK]) loginMemberInfo: LoginMemberInfo,
        @PathVariable orderId: Long
    ) {
       cancelOrderUseCase.cancel(CancelOrderCommand(loginMemberInfo.memberId, orderId))
    }
}