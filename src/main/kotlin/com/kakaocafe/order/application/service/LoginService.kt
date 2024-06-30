package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.LoginCommand
import com.kakaocafe.order.application.port.`in`.LoginUseCase
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.LoginMember
import com.kakaocafe.order.domain.Token
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import org.apache.juli.logging.Log
import org.springframework.stereotype.Service

@Service
class LoginService(private val memberPersistencePort: MemberPersistencePort) : LoginUseCase {
    override fun login(loginCommand: LoginCommand): LoginMember {
        val member = memberPersistencePort.findByEmail(loginCommand.email)

        if (member == null || !member.isEnable()) {
            throw KakaoCafeException(ErrorCode.INVALID_CREDENTIALS)
        }

        if (!member.isCorrectPassword(loginCommand.password)) {
            throw KakaoCafeException(ErrorCode.INVALID_CREDENTIALS)
        }

        return LoginMember(member.status, Token.of(member))
    }
}