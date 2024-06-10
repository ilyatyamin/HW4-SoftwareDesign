package com.example.ticketservice.dto

data class InfoResponse(
    var message : String,
    var userId : Long? = null,
    var email : String? = null,
    var isExpired : Boolean? = null
)
