package com.example.authservice.controllers

import com.example.authservice.services.AuthService
import com.example.authservice.dto.RegistrationRequest
import com.example.authservice.dto.RegistrationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val answer = authService.registerUser(request)
        return ResponseEntity.status(answer.status).body(answer.info)
    }
}