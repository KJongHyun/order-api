package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.JoinMemberCommand
import com.kakaocafe.order.application.port.LoginCommand
import com.kakaocafe.order.application.port.QuitMemberCommand
import com.kakaocafe.order.application.port.`in`.JoinMemberUseCase
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Member
import com.kakaocafe.order.domain.Token
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import org.springframework.stereotype.Service

@Service
class JoinMemberService(
    private val memberPersistencePort: MemberPersistencePort,
    private val lockService: LockService
) : JoinMemberUseCase {
    override fun join(joinMemberCommand: JoinMemberCommand) {
        lockService.lockScope(joinMemberCommand.email) {

            if (memberPersistencePort.existEmail(joinMemberCommand.email)) {
                throw KakaoCafeException(ErrorCode.DUPLICATE_EMAIL)
            }

            val member = Member(
                email = joinMemberCommand.email,
                password = joinMemberCommand.password,
                name = joinMemberCommand.name,
                gender = joinMemberCommand.gender,
                phoneNumber = joinMemberCommand.phoneNumber,
                birthDay = joinMemberCommand.birthDay,
            ).apply { join() }

            memberPersistencePort.save(member)

        }
    }
}