package com.kakaocafe.order.adapter.`in`.web.member.request


import com.kakaocafe.order.application.port.LoginCommand
import com.kakaocafe.order.application.port.Password
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequestDto(
    @Email
    val email: String,
    @NotBlank
    val password: String,
) {
    fun toCommand(): LoginCommand {
        return LoginCommand(
            email = email,
            password = Password.fromPlainPassword(password)
        )
    }
}



