package com.kakaocafe.order.global

import com.kakaocafe.order.adapter.out.persistence.entity.ProductEntity
import com.kakaocafe.order.adapter.out.persistence.jpaRepository.ProductEntityJpaRepository
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import java.math.BigDecimal

/**
 * 주문 기능과 별개로 테스트를 위하여 서버를 초기화하는 기능입니다
 */
@Configuration
class InitConfiguration(private val productEntityJpaRepository: ProductEntityJpaRepository) {
    @EventListener(ApplicationStartedEvent::class)
    fun initProject() {
        val products = listOf(
            ProductEntity(
                name = "상품1",
                price = BigDecimal(5000),
                stock = 10
            ),
            ProductEntity(
                name = "상품2",
                price = BigDecimal(6000),
                stock = 7
            ),
            ProductEntity(
                name = "상품3",
                price = BigDecimal(6000),
                stock = 9
            ),
            ProductEntity(
                name = "상품4",
                price = BigDecimal(5500),
                stock = 13
            )
        )

        productEntityJpaRepository.saveAll(products)
    }
}