package com.kakaocafe.order.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

enum class ErrorCode(val httpStatusCode: HttpStatusCode, val message: String) {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일"),
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원"),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "잘못된 회원 정보"),
    ALREADY_QUIT_MEMBER(HttpStatus.CONFLICT, "이미 탈퇴한 회원"),
    UNAUTHORIZED_API(HttpStatus.UNAUTHORIZED, "현재 회원 상태로 접근할 수 없는 API 입니다."),

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰"),
    NOT_EXIST_TOKEN(HttpStatus.NOT_FOUND, "토큰 정보 없음"),

    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "데이터 없음"),

    ORDER_ERROR(HttpStatus.BAD_REQUEST,  "주문 에러"),
    CANNOT_CANCEL_ORDER(HttpStatus.CONFLICT, "주문 취소 불가능한 상태"),
    NOT_EXIST_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 주문"),
    NOT_ENOUGH_STOCK(HttpStatus.CONFLICT, "재고 부족"),

    PAY_ERROR(HttpStatus.BAD_REQUEST, "결제 에러"),
    NOT_EXIST_PAYMENT(HttpStatus.NOT_FOUND, "존재하지 않는 결제건"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    ETC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "기타 에러")
}