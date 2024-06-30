package com.kakaocafe.order.application.port

data class LoginCommand(
    val email: String,
    val password: Password
)

@JvmInline
value class Password (
    val value: String
) {
    companion object {
        fun fromPlainPassword(plainPassword: String): Password {
            // 암호화 로직 생략
            return Password(plainPassword)
        }
    }
}
