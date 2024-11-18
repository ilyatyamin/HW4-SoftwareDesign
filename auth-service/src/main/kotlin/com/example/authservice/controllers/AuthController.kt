package com.example.authservice.controllers

import com.example.authservice.dto.*
import com.example.authservice.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/register")
    @Operation(summary = "Register user if all data is correctly filled.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully add user to database"),
            ApiResponse(responseCode = "400", description = "The user entered incorrect data: the password or email address did not arrive or entered an incorrect form (one of the fields is null)"),
            ApiResponse(responseCode = "409", description = "User with this data already existed in system.")
        ])
    fun register(@org.springframework.web.bind.annotation.RequestBody
                 @RequestBody(content = [Content(schema = Schema(implementation = RegistrationRequest::class))])
                 request: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val answer = authService.registerUser(request)
        return ResponseEntity.status(answer.status).body(answer.info)
    }



    @PostMapping("/login")
    @Operation(summary = "Login user and send back user's token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "The user is in the database, everything is correct"),
            ApiResponse(responseCode = "400", description = "The user entered incorrect data: one of the fields possible null"),
            ApiResponse(responseCode = "403", description = "User with this data is not in the database.")
        ])
    fun login(@org.springframework.web.bind.annotation.RequestBody
              @RequestBody(content = [Content(schema = Schema(implementation = AuthRequest::class))])
              request: AuthRequest): ResponseEntity<AuthResponse> {
        val answer = authService.tryToLogin(request)
        return ResponseEntity.status(answer.status).body(answer.info)
    }



    @PostMapping("/getInfo")
    @Operation(summary = "Get info about user by token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Everything is OK"),
            ApiResponse(responseCode = "403", description = "The received token does not exist")
        ])
    fun getInfo(@org.springframework.web.bind.annotation.RequestBody
                @RequestBody(content = [Content(schema = Schema(implementation = InfoRequest::class))])
                request: InfoRequest): ResponseEntity<InfoResponse> {
        val answer = authService.getInfo(request)
        return ResponseEntity.status(answer.status).body(answer.info)
    }

}