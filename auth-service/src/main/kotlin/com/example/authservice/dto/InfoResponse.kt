package com.example.authservice.dto

data class InfoResponse(
    var message : String,
    var userId : Long? = null,
    var email : String? = null,
    var isExpired : Boolean? = null
)
