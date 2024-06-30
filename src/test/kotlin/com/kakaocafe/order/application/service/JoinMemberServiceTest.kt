package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.JoinMemberCommand
import com.kakaocafe.order.application.port.Password
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Gender
import com.kakaocafe.order.domain.Member
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

class JoinMemberServiceTest {

    private val memberPersistencePort: MemberPersistencePort = mock()
    private val lockService: LockService = mock()
    private val joinMemberService: JoinMemberService = JoinMemberService(memberPersistencePort, lockService)

    @Test
    fun `중복 이메일이면 DUPLICATE_EMAIL 예외를 발생시킴`() {
        val joinMemberCommand = JoinMemberCommand(
            email = "test@example.com",
            password = Password.fromPlainPassword("plainPassword"),
            name = "Test User",
            gender = Gender.MALE,
            phoneNumber = "1234567890",
            birthDay = LocalDate.of(1992, 6, 20)
        )

        given(memberPersistencePort.existEmail(joinMemberCommand.email)).willReturn(true)
        doAnswer { invocation ->
            val block = invocation.getArgument<() -> Unit>(2)
            block.invoke()
            null
        }.whenever(lockService).lockScope<Any>(key = any(), any(), any())

        val exception = shouldThrow<KakaoCafeException> {
            joinMemberService.join(joinMemberCommand)
        }

        exception.errorCode shouldBe ErrorCode.DUPLICATE_EMAIL
    }

    @Test
    fun `회원가입이 성공적으로 수행됨`() {
        val joinMemberCommand = JoinMemberCommand(
            email = "test@example.com",
            password = Password.fromPlainPassword("plainPassword"),
            name = "Test User",
            gender = Gender.MALE,
            phoneNumber = "1234567890",
            birthDay = LocalDate.of(1992, 6, 20)
        )

        given(memberPersistencePort.existEmail(joinMemberCommand.email)).willReturn(false)
        doAnswer { invocation ->
            val block = invocation.getArgument<() -> Unit>(2)
            block.invoke()
            null
        }.whenever(lockService).lockScope<Any>(key = any(), any(), any())

        joinMemberService.join(joinMemberCommand)

        verify(memberPersistencePort).save(any<Member>())
    }
}
