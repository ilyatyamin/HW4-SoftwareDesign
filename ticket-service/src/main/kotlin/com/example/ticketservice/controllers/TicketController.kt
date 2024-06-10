package com.example.ticketservice.controllers

import com.example.ticketservice.dto.*
import com.example.ticketservice.services.NetworkService
import com.example.ticketservice.services.OrderService
import com.example.ticketservice.services.ProcessService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
    private val networkService: NetworkService,
    private val processService: ProcessService
) {
    init {
        processService.startProcessing()
    }


    @PostMapping("/buyTicket")
    @Operation(summary = "Form an order if all data is correct and user is authorized")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
        value = [ ApiResponse(responseCode = "200", description = "Successfully add order to database"),
            ApiResponse(responseCode = "400", description = "User sent incorrect data"),
            ApiResponse(responseCode = "401", description = "User is unauthorized (please put JWT-token is Authorization header field)")])
    fun buyTicket(
        @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Ticket service",
            content = [Content(schema = Schema(implementation = BuyTicketRequest::class))])
        @RequestBody request: BuyTicketRequest,
        @RequestHeader("Authorization") token: String? = null
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


    @PostMapping("/getTicketInfo")
    @Operation(summary = "Send ticket info if order id is correct and user is authorized")
    @ApiResponses(
        value = [ ApiResponse(responseCode = "200", description = "Successfully add order to database"),
            ApiResponse(responseCode = "400", description = "User sent incorrect data"),
            ApiResponse(responseCode = "401", description = "User is unauthorized (please put JWT-token is Authorization header field)")])
    fun getTicketInfo(
        @RequestBody request: GetTicketRequest
    ): ResponseEntity<GetTicketResponse> {
        val answer = ticketService.getTicketInfo(request)
        return ResponseEntity.status(answer.status).body(answer.info)
    }
}