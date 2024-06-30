package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.QuitMemberCommand
import com.kakaocafe.order.application.port.`in`.QuitMemberUseCase
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import org.springframework.stereotype.Service

@Service
class QuitMemberService(private val memberPersistencePort: MemberPersistencePort) : QuitMemberUseCase {
    override fun quit(quitMemberCommand: QuitMemberCommand) {
        val member = memberPersistencePort.findById(quitMemberCommand.memberId) ?: throw KakaoCafeException(ErrorCode.NOT_EXIST_MEMBER)
        member.quit()

        memberPersistencePort.save(member)
    }
}