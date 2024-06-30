package com.kakaocafe.order.adapter.`in`.web.member.response

import com.kakaocafe.order.domain.MemberStatus
import com.kakaocafe.order.domain.Token

data class LoginResponseDto(
    val token: Token,
    val memberStatus: MemberStatus
)
