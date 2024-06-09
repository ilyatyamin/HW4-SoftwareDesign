package com.example.ticketservice.repositories

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Int, Long> {

}