package com.example.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RegistrationRequest(
    @Schema(example = "my_nickname", nullable = false)
    var nickname: String?,

    @Schema(
        example = "myemail@gmail.com",
        nullable = false,
        format = "must contains @ and . and be at least 5 symbols in size"
    )
    var email: String?,

    @Schema(
        example = "reallyGood12345!",
        nullable = false,
        format = "must contains upper and lower symbols, numbers at be at least 8 symbols in size "
    )
    var password: String?
)
