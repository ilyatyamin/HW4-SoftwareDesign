package com.example.ticketservice.dto

import com.example.ticketservice.dao.Order

data class GetTicketResponse(
    var message : String,
    var order : Order? = null
)
