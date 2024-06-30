package com.kakaocafe.order.adapter.`in`.web.payment

import com.kakaocafe.order.adapter.`in`.web.member.LoginMember
import com.kakaocafe.order.adapter.`in`.web.member.LoginMemberInfo
import com.kakaocafe.order.adapter.`in`.web.payment.request.PaymentRequestDto
import com.kakaocafe.order.application.port.PayCommand
import com.kakaocafe.order.application.port.`in`.PayUseCase
import com.kakaocafe.order.domain.MemberStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/payments")
class PaymentController(private val payUseCase: PayUseCase) {
    @PostMapping
    fun payment(@LoginMember([MemberStatus.OK]) loginMemberInfo: LoginMemberInfo, @RequestBody paymentRequestDto: PaymentRequestDto) {
        payUseCase.pay(PayCommand(loginMemberInfo.memberId, paymentRequestDto.orderId, paymentRequestDto.amount))
    }
}