package com.kakaocafe.order.domain

import com.kakaocafe.order.application.port.Password
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import java.time.LocalDate
import java.time.LocalDateTime

class Member(
    val id: Long = 0,
    val email: String,
    val password: Password,
    val name: String,
    val gender: Gender,
    val phoneNumber: String,
    val birthDay: LocalDate,
    var quitAt: LocalDateTime? = null,
    var status: MemberStatus = MemberStatus.OK
) {
    companion object {
        const val WITHDRAWAL_REVERSAL_PERIOD_DAYS = 30L
    }

    fun join() {
        status = MemberStatus.OK
        quitAt = null
    }

    fun quit() {
        if (status == MemberStatus.SUSPENDED_QUIT || status == MemberStatus.QUIT) {
            throw KakaoCafeException(ErrorCode.ALREADY_QUIT_MEMBER)
        }

        status = MemberStatus.SUSPENDED_QUIT
        quitAt = LocalDateTime.now()
    }

    fun isEnable(): Boolean {
        return when (this.status) {
            MemberStatus.OK -> true
            MemberStatus.SUSPENDED_QUIT -> {
                quitAt?.isBefore(LocalDateTime.now().minusDays(WITHDRAWAL_REVERSAL_PERIOD_DAYS)) == false
            }
            MemberStatus.QUIT -> false
        }
    }

    fun isCorrectPassword(password: Password): Boolean {
        return this.password == password
    }
}

enum class Gender {
    MALE,
    FEMALE,
    ;
}

enum class MemberStatus(val description: String) {
    OK("정상"), QUIT("탈퇴"), SUSPENDED_QUIT("탈퇴 유예 기간")
    ;
}