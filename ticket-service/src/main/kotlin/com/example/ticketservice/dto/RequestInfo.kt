package com.example.ticketservice.dto

data class RequestInfo<T>(
    val info : T,
    val aboutUser : InfoResponse
)