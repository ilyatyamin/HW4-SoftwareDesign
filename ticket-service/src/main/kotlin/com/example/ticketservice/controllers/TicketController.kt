package com.example.ticketservice.controllers

import com.example.ticketservice.dto.BuyTicketRequest
import com.example.ticketservice.dto.BuyTicketResponse
import com.example.ticketservice.dto.RequestInfo
import com.example.ticketservice.services.NetworkService
import com.example.ticketservice.services.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController(
    private val ticketService: OrderService,
    private val networkService: NetworkService
) {
    @PostMapping("/buyTicket")
    fun buyTicket(
        @RequestBody request: BuyTicketRequest,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<BuyTicketResponse> {
        val getInfo = networkService.makeConnectionToAuthService("/auth/getInfo", token)
        return if (getInfo == null) {
            ResponseEntity.status(400).body(BuyTicketResponse(message = "You sent incorrect data"))
        } else {
            val answer = ticketService.processOrder(
                RequestInfo(
                    info = request,
                    aboutUser = getInfo
                )
            )
            ResponseEntity.status(answer.status).body(answer.info)
        }
    }
}