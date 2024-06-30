package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.JoinMemberCommand

interface JoinMemberUseCase {
    fun join(joinMemberCommand: JoinMemberCommand)
}