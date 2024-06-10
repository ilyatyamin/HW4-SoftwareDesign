package com.example.ticketservice.utils

import org.springframework.context.annotation.Configuration

@Configuration
class PortsConfiguration {
    val authPort = "8080"
    val ticketPort = "8081"
}