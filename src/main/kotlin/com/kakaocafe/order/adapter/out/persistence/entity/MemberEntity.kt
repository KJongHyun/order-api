package com.kakaocafe.order.adapter.out.persistence.entity

import com.kakaocafe.order.application.port.Password
import com.kakaocafe.order.domain.Gender
import com.kakaocafe.order.domain.Member
import com.kakaocafe.order.domain.MemberStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long,
    @Enumerated(EnumType.STRING)
    var status: MemberStatus,
    val email: String,
    val password: Password,
    val name: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    val phoneNumber: String,
    val birthDay: LocalDate,
    val quitAt: LocalDateTime?
) {
    companion object {
        fun of(member: Member): MemberEntity {
            return MemberEntity(
                id = member.id,
                status = member.status,
                email = member.email,
                password = member.password,
                name = member.name,
                gender = member.gender,
                phoneNumber = member.phoneNumber,
                birthDay = member.birthDay,
                quitAt = member.quitAt,
            )
        }
    }

    fun toDomain(): Member {
        return Member(
            id = this.id,
            status = this.status,
            email = this.email,
            password = this.password,
            name = this.name,
            gender = this.gender,
            phoneNumber = this.phoneNumber,
            birthDay = this.birthDay,
            quitAt = this.quitAt,
        )
    }
}