package com.kakaocafe.order.adapter.`in`.web.member

import com.kakaocafe.order.adapter.`in`.web.member.request.JoinMemberRequestDto
import com.kakaocafe.order.adapter.`in`.web.member.request.LoginRequestDto
import com.kakaocafe.order.adapter.`in`.web.member.response.LoginResponseDto
import com.kakaocafe.order.application.port.QuitMemberCommand
import com.kakaocafe.order.application.port.`in`.JoinMemberUseCase
import com.kakaocafe.order.application.port.`in`.LoginUseCase
import com.kakaocafe.order.application.port.`in`.QuitMemberUseCase
import com.kakaocafe.order.domain.MemberStatus
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val joinMemberUseCase: JoinMemberUseCase,
    private val loginUseCase: LoginUseCase,
    private val quitMemberUseCase: QuitMemberUseCase
) {

    @PostMapping("/join")
    fun join(@RequestBody @Valid joinMemberRequestDto: JoinMemberRequestDto) {
        joinMemberUseCase.join(joinMemberRequestDto.toCommand())
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDto): LoginResponseDto {
        val loginMember = loginUseCase.login(loginRequestDto.toCommand())

        return LoginResponseDto(loginMember.token, loginMember.memberStatus)
    }

    @DeleteMapping("/quit")
    fun quit(@LoginMember([MemberStatus.OK]) loginMemberInfo: LoginMemberInfo) {
        quitMemberUseCase.quit(QuitMemberCommand(loginMemberInfo.memberId))
    }
}