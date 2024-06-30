package com.kakaocafe.order.adapter.out.persistence

import com.kakaocafe.order.adapter.out.persistence.entity.MemberEntity
import com.kakaocafe.order.adapter.out.persistence.jpaRepository.MemberEntityJpaRepository
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Member
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MemberPersistenceAdapter(private val memberEntityJpaRepository: MemberEntityJpaRepository) : MemberPersistencePort {
    override fun save(member: Member): Member {
        return memberEntityJpaRepository.save(MemberEntity.of(member)).toDomain()
    }

    override fun findById(id: Long): Member? {
        return memberEntityJpaRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findByEmail(email: String): Member? {
        return memberEntityJpaRepository.findByEmail(email)?.toDomain()
    }

    override fun existEmail(email: String): Boolean {
        return memberEntityJpaRepository.existsByEmail(email)
    }
}