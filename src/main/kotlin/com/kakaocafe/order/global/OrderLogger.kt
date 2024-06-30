package com.kakaocafe.order.global

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface OrderLogger {
    val log: Logger get() = LoggerFactory.getLogger(this.javaClass)
}