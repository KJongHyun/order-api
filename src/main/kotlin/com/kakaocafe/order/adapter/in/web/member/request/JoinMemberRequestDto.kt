package com.kakaocafe.order.adapter.`in`.web.member.request

import com.kakaocafe.order.application.port.JoinMemberCommand
import com.kakaocafe.order.application.port.Password
import com.kakaocafe.order.domain.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class JoinMemberRequestDto(
    @Email
    val email: String,
    @NotBlank
    val password: String,
    @NotBlank
    val name: String,
    val gender: Gender,
    val phoneNumber: String,
    val birthDay: LocalDate,
) {
    fun toCommand(): JoinMemberCommand {
        return JoinMemberCommand(
            email = email,
            password = Password.fromPlainPassword(password),
            name = name,
            gender = gender,
            phoneNumber = phoneNumber,
            birthDay = birthDay
        )
    }
}
