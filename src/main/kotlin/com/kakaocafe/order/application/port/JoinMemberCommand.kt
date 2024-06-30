package com.kakaocafe.order.application.port

import com.kakaocafe.order.domain.Gender
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class JoinMemberCommand(
    val email: String,
    val password: Password,
    val name: String,
    val gender: Gender,
    val phoneNumber: String,
    val birthDay: LocalDate,
)
