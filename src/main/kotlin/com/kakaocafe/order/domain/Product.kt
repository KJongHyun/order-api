package com.kakaocafe.order.domain

import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import java.math.BigDecimal

class Product(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    var stock: Int
) {
    fun reduceStockForOrder(orderCount: Int) {
        if (stock < orderCount)
            throw KakaoCafeException(ErrorCode.NOT_ENOUGH_STOCK, "재고가 부족한 상품 productId:$id")
        stock -= orderCount
    }

    fun restoreStock(restoreCount: Int) {
        stock += restoreCount
    }
}