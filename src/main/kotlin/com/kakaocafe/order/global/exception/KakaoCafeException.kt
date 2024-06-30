package com.kakaocafe.order.global.exception

class KakaoCafeException(val errorCode: ErrorCode, message: String) : RuntimeException(message) {

    constructor(errorCode: ErrorCode): this(errorCode, errorCode.message)
}