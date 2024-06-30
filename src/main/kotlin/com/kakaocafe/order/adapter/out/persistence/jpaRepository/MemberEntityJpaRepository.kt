package com.kakaocafe.order.adapter.out.persistence.jpaRepository

import com.kakaocafe.order.adapter.out.persistence.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberEntityJpaRepository : JpaRepository<MemberEntity, Long> {
    fun findByEmail(email: String): MemberEntity?
    fun existsByEmail(email: String): Boolean
}