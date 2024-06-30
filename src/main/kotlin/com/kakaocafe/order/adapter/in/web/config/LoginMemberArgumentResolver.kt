package com.kakaocafe.order.adapter.`in`.web.config

import com.kakaocafe.order.adapter.`in`.web.member.LoginMember
import com.kakaocafe.order.adapter.`in`.web.member.LoginMemberInfo
import com.kakaocafe.order.application.port.out.MemberPersistencePort
import com.kakaocafe.order.domain.Token
import com.kakaocafe.order.global.exception.ErrorCode
import com.kakaocafe.order.global.exception.KakaoCafeException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(private val memberPersistencePort: MemberPersistencePort) : HandlerMethodArgumentResolver {

    companion object {
        const val TOKEN_HEADER = "x-kakaocafe-token"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(LoginMember::class.java) != null && parameter.parameterType == LoginMemberInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val loginToken = webRequest.getHeader(TOKEN_HEADER) ?: throw KakaoCafeException(ErrorCode.NOT_EXIST_TOKEN)
        val loginMemberInfo = Token(loginToken).getLoginMemberInfo()
        val member = memberPersistencePort.findById(loginMemberInfo.memberId) ?: throw KakaoCafeException(ErrorCode.NOT_EXIST_MEMBER)

        val loginMemberAnnotation = parameter.getParameterAnnotation(LoginMember::class.java)

        return if (loginMemberAnnotation != null && member.status in loginMemberAnnotation.validStatuses)
            LoginMemberInfo(member.id, member.email, member.status)
        else {
            throw KakaoCafeException(ErrorCode.UNAUTHORIZED_API)
        }
    }
}