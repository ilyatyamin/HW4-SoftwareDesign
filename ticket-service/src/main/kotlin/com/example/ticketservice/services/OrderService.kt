package com.example.ticketservice.services

import com.example.ticketservice.dao.Order
import com.example.ticketservice.dto.*
import com.example.ticketservice.repositories.OrderRepository
import com.example.ticketservice.repositories.StationRepository
import com.example.ticketservice.utils.StatusOrder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val stationRepository: StationRepository
) {
    fun processOrder(request: RequestInfo<BuyTicketRequest>): StatusInfo<BuyTicketResponse> {
        if (stationRepository.existsStationById(request.info.departureId) &&
            stationRepository.existsStationById(request.info.arrivalId)
        ) {
            val order = Order(
                userId = request.aboutUser.userId,
                fromStationId = request.info.departureId,
                toStationId = request.info.arrivalId,
                status = 1,
                created = LocalDateTime.now()
            )

            orderRepository.save(order)
            return StatusInfo(
                status = HttpStatus.OK,
                info = BuyTicketResponse(
                    message = "Your order was created with id = ${order.id}",
                    orderId = order.id
                )
            )
        } else {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = BuyTicketResponse(message = "This departureID or arrivalID doesn't exists.")
            )
        }
    }

    fun getTicketInfo(request: GetTicketRequest): StatusInfo<GetTicketResponse> {
        if (orderRepository.existsOrderById(request.id)) {
            val order = orderRepository.findOrderById(request.id)
            return StatusInfo(
                status = HttpStatus.OK,
                info = GetTicketResponse(
                    message = "OK",
                    order = order
                )
            )
        } else {
            return StatusInfo(
                status = HttpStatus.NOT_FOUND,
                info = GetTicketResponse(
                    message = "Order with this id doesn't exists",
                )
            )
        }
    }
}