package com.example.ticketservice.dto

import io.swagger.v3.oas.annotations.media.Schema

data class BuyTicketRequest(
    @Schema(example = "1", format = "positive number, in DB in standart nums from 1 to 1000")
    val departureId: Int,

    @Schema(example = "1", format = "positive number, in DB in standart nums from 1 to 1000")
    val arrivalId: Int
)

