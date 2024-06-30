package com.kakaocafe.order.application.service

import com.kakaocafe.order.application.port.LoginCommand
import com.kakaocafe.order.application.port.Password
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Member
import com.kakaocafe.order.domain.MemberStatus
import com.kakaocafe.order.domain.Token
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDateTime

class LoginServiceTest {

    private val memberPersistencePort: MemberPersistencePort = mock()
    private val loginService: LoginService = LoginService(memberPersistencePort)

    @Test
    fun `존재하지 않는 이메일이면 INVALID_CREDENTIALS 예외를 발생시킴`() {
        val loginCommand = LoginCommand(email = "nonexistent@example.com", password = Password.fromPlainPassword("plainPassword"))

        given(memberPersistencePort.findByEmail(loginCommand.email)).willReturn(null)

        val exception = shouldThrow<KakaoCafeException> {
            loginService.login(loginCommand)
        }

        exception.errorCode shouldBe ErrorCode.INVALID_CREDENTIALS
    }

    @Test
    fun `비활성화된 계정이면 INVALID_CREDENTIALS 예외를 발생시킴`() {
        val member = mock<Member> {
            on { isEnable() } doReturn false
        }
        val loginCommand = LoginCommand(email = "disabled@example.com", password = Password.fromPlainPassword("plainPassword"))

        given(memberPersistencePort.findByEmail(loginCommand.email)).willReturn(member)

        val exception = shouldThrow<KakaoCafeException> {
            loginService.login(loginCommand)
        }

        exception.errorCode shouldBe ErrorCode.INVALID_CREDENTIALS
    }

    @Test
    fun `비밀번호가 틀리면 INVALID_CREDENTIALS 예외를 발생시킴`() {
        val member = mock<Member> {
            on { isEnable() } doReturn true
            on { isCorrectPassword(Password.fromPlainPassword("plainPassword")) } doReturn false
        }
        val loginCommand = LoginCommand(email = "user@example.com", password = Password.fromPlainPassword("plainPassword"))

        given(memberPersistencePort.findByEmail(loginCommand.email)).willReturn(member)

        val exception = shouldThrow<KakaoCafeException> {
            loginService.login(loginCommand)
        }

        exception.errorCode shouldBe ErrorCode.INVALID_CREDENTIALS
    }

    @Test
    fun `정상적인 로그인 성공`() {
        val member = mock<Member> {
            on { isEnable() } doReturn true
            on { isCorrectPassword(Password.fromPlainPassword("plainPassword")) } doReturn true
            on { status } doReturn MemberStatus.OK
        }
        val loginCommand = LoginCommand(email = "user@example.com", password = Password.fromPlainPassword("plainPassword"))

        given(memberPersistencePort.findByEmail(loginCommand.email)).willReturn(member)
        given(member.isCorrectPassword(loginCommand.password)).willReturn(true)

        val tokenCreateTime = LocalDateTime.now()

        mockStatic(LocalDateTime::class.java).use {
            it.`when`<LocalDateTime> { LocalDateTime.now() }.thenReturn(tokenCreateTime)
            val loginMember = loginService.login(loginCommand)

            loginMember.memberStatus shouldBe MemberStatus.OK
            loginMember.token shouldBe Token.of(member)
        }
    }
}
