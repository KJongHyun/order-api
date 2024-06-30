package com.kakaocafe.order.adapter.`in`.web.member

import com.kakaocafe.order.domain.MemberStatus

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember(
    val validStatuses: Array<MemberStatus> = [MemberStatus.OK]
)

