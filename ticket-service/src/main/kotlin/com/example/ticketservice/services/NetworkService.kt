package com.example.ticketservice.services

import com.example.ticketservice.dto.InfoRequest
import com.example.ticketservice.dto.InfoResponse
import com.example.ticketservice.utils.PortsConfiguration
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class NetworkService(val config: PortsConfiguration) {
    private val httpClient = WebClient.create()

    fun makeConnectionToAuthService(
        branch: String,
        authHeader: String?
    ): InfoResponse? {
        // Если не используется Bearer, то переходим к следующей проверке
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        // Получим токен
        val token = authHeader.substring(7)

        return httpClient.post()
            .uri("http://auth-service:${config.authPort}${branch}")
            .bodyValue(InfoRequest(token = token))
            .retrieve()
            .bodyToMono(InfoResponse::class.java)
            .block()
    }
}