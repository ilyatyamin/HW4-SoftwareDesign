package com.example.ticketservice.dto

data class BuyTicketResponse(
    val message: String,
    val orderId: Long? = null
)
