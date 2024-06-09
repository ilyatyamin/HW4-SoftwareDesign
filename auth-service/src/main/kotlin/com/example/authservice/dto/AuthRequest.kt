package com.example.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AuthRequest(
    @Schema(example = "my_email@gmail.com",
        format = "email from account that has registered earlier")
    var email: String,

    @Schema(example = "MyReallyGood12345!",
        format = "password from account that has registered earlier")
    var password: String
)
