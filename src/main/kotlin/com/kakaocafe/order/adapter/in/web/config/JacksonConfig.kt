package com.kakaocafe.order.adapter.`in`.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakaocafe.order.global.DEFAULT_OBJECT_MAPPER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return DEFAULT_OBJECT_MAPPER
    }
}