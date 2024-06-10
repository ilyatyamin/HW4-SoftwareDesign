package com.example.authservice.dto

data class AuthResponse(
    var message : String,
    var token : String? = null
)
