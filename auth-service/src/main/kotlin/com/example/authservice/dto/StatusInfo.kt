package com.example.authservice.dto

import org.springframework.http.HttpStatus

data class StatusInfo<T>(
    val status : HttpStatus,
    val info : T
)