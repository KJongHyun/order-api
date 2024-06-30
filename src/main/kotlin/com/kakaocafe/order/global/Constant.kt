package com.kakaocafe.order.global

import com.fasterxml.jackson.databind.ObjectMapper
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

val DEFAULT_OBJECT_MAPPER =  Jackson2ObjectMapperBuilder().build<ObjectMapper>()

fun <R> withTimeout(timeoutSeconds: Long, block: () -> R): R = runBlocking {
    runCatching {
        kotlinx.coroutines.withTimeout(timeoutSeconds * 1000) {
            block()
        }
    }.onFailure {
        when (it) {
            is TimeoutCancellationException -> throw KakaoCafeException(ErrorCode.INTERNAL_SERVER_ERROR, "타임아웃 에러")
            else -> throw it
        }
    }.getOrThrow()
}