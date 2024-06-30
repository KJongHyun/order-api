package com.kakaocafe.order.adapter.`in`.web.member

import com.kakaocafe.order.domain.MemberStatus

data class LoginMemberInfo(
    val memberId: Long,
    val email: String,
    val memberStatus: MemberStatus
)
