package com.example.ticketservice.dto

import io.swagger.v3.oas.annotations.media.Schema

data class GetTicketRequest(
    @Schema(example = "1", format = "positive number")
    var id : Long
)
