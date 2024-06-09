package com.example.ticketservice.repositories

import com.example.ticketservice.dao.Station
import org.springframework.data.jpa.repository.JpaRepository

interface StationRepository : JpaRepository<Station, Long> {
}