package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.LoginCommand
import com.kakaocafe.order.domain.LoginMember

interface LoginUseCase {
    fun login(loginCommand: LoginCommand): LoginMember
}