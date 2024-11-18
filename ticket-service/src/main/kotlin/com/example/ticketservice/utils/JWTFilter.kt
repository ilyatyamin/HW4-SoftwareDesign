package com.example.ticketservice.utils

import com.example.ticketservice.dto.InfoRequest
import com.example.ticketservice.dto.InfoResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.reactive.function.client.WebClient

@Component
@Order(1)
class JWTFilter(private val config: PortsConfiguration) : OncePerRequestFilter() {
    private val httpClient = WebClient.create()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Получим хэдер авторизации
        try {
            val authHeader = request.getHeader("Authorization")

            // Если не используется Bearer, то переходим к следующей проверке
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }

            // Получим токен
            val token = authHeader.substring(7)

            // Отошлем запрос на первый микросервис и узнаем инфу про токен
            val message = httpClient.post()
                .uri("http://auth-service:${config.authPort}/auth/getInfo")
                .bodyValue(InfoRequest(token = token))
                .retrieve()
                .bodyToMono(InfoResponse::class.java)
                .block()

            if (message?.isExpired == true) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED;
                return
            } else {
                response.status = HttpServletResponse.SC_OK
                filterChain.doFilter(request, response)
            }
        } catch (ex: Exception) {
            println(ex.message)
            response.status = HttpServletResponse.SC_UNAUTHORIZED;
            return;
        }
    }
}