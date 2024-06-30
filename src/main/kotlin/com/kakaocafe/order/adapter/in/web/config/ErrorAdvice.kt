package com.kakaocafe.order.adapter.`in`.web.config

import com.kakaocafe.order.global.OrderLogger
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorAdvice {

    companion object : OrderLogger

    @ExceptionHandler(KakaoCafeException::class)
    fun kakaoCafeException(request: HttpServletRequest, e: KakaoCafeException): ResponseEntity<ErrorResponse> {
        log.error("@@ERROR@@ errorCode:${e.errorCode.name}, errorMessage:${e.errorCode.message}",e)
        return ResponseEntity.status(e.errorCode.httpStatusCode).body(ErrorResponse(e.errorCode))
    }

    @ExceptionHandler(Exception::class)
    fun internalException(request: HttpServletRequest, e: Exception): ResponseEntity<ErrorResponse> {
        log.error("@@ERROR@@ internal server error",e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR))
    }

    class ErrorResponse(
        val code: String,
        val message: String
    ) {
        constructor(errorCode: ErrorCode) : this(errorCode.name, errorCode.message)
    }
}