package com.example.ticketservice.dto

data class BuyTicketRequest(
    val userId : Int,
    val departureId : Int,
    val arrivalId : Int
)

