package com.example.ticketservice.dto

import org.springframework.http.HttpStatus

data class StatusInfo<T>(
    val status : HttpStatus,
    val info : T
)