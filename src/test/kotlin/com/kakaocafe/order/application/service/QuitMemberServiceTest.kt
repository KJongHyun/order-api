package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.QuitMemberCommand
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Member
import com.kakaocafe.order.domain.MemberStatus
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class QuitMemberServiceTest {
    private val memberPersistencePort: MemberPersistencePort = mock()
    private val quitMemberService: QuitMemberService = QuitMemberService(memberPersistencePort)

    @Test
    fun `존재하지 않는 회원이면 NOT_EXIST_MEMBER 예외를 발생시킴`() {
        val quitMemberCommand = QuitMemberCommand(memberId = 1)

        given(memberPersistencePort.findById(quitMemberCommand.memberId)).willReturn(null)

        val exception = shouldThrow<KakaoCafeException> {
            quitMemberService.quit(quitMemberCommand)
        }

        exception.errorCode shouldBe ErrorCode.NOT_EXIST_MEMBER
    }

    @Test
    fun `정상적인 회원 탈퇴`() {
        val member = mock<Member> {
            on { status } doReturn MemberStatus.OK
        }
        val quitMemberCommand = QuitMemberCommand(memberId = 1)
        given(memberPersistencePort.findById(quitMemberCommand.memberId)).willReturn(member)

        quitMemberService.quit(quitMemberCommand)

        verify(member).quit()
        verify(memberPersistencePort).save(member)
    }
}