package com.example.authservice.dto

data class RegistrationRequest(
    var nickname : String?,
    var email : String?,
    var password : String?
)
