package com.example.ticketservice.dto

import io.swagger.v3.oas.annotations.media.Schema

data class InfoRequest(
    @Schema(format = "token")
    val token : String
)
