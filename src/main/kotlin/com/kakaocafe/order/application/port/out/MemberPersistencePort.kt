package com.kakaocafe.order.application.port.out

import com.kakaocafe.order.domain.Member

interface MemberPersistencePort {
    fun save(member: Member): Member
    fun findById(id: Long): Member?
    fun findByEmail(email: String): Member?
    fun existEmail(email: String): Boolean
}