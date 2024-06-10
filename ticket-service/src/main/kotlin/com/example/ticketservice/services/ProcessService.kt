package com.example.ticketservice.services

import com.example.ticketservice.repositories.OrderRepository
import com.example.ticketservice.utils.StatusOrder
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ProcessService(var orderRepository: OrderRepository) {
    private lateinit var thread : Thread

    fun startProcessing() {
        thread = Thread { threadFunc() }
        thread.start()
    }

    fun stopProcessing() {
        try {
            thread.interrupt()
        } catch (ex : Exception) {
            println(ex.message)
        }
    }

    private fun threadFunc() {
        while (true) {
            try {
                val getLst = orderRepository.findOrdersByStatus(StatusOrder.CHECK)
                val randomOrder = getLst.random()

                val id = (0..100).random()
                if (id % 2 == 0) {
                    randomOrder.status = 2
                } else {
                    randomOrder.status = 3
                }
                orderRepository.save(randomOrder)
            } catch (ex : Exception) {
                println(ex)
            }

            Thread.sleep(10000) // sleep for 3000 seconds
        }
    }
}