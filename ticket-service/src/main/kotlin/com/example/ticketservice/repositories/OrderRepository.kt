package com.example.ticketservice.repositories

import com.example.ticketservice.dao.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {

}