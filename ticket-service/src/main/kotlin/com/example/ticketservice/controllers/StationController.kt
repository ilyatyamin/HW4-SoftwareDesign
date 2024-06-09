package com.example.ticketservice.controllers

import com.example.ticketservice.dao.Station
import com.example.ticketservice.repositories.StationRepository
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Controller
class StationController(private val stationRepo: StationRepository) {
    init {
        try {
            val client = WebClient.create()

            val response = client.get()
                .uri("https://gist.githubusercontent.com/helart/96225136a784f8a3987398be96456dce/raw/8d4b63baf056ca0680c6fc18fc76f17c83525c28/txt-cities-russia.txt")
                .retrieve()
                .bodyToMono<String>().block()

            for (city in response!!.split("\n")) {
                stationRepo.save(Station(station = city))
            }
        } catch (ex: Exception) {
            stationRepo.save(Station(station = "Москва"))
            stationRepo.save(Station(station = "Санкт-Петербург"))
            stationRepo.save(Station(station = "Казань"))
            stationRepo.save(Station(station = "Мурманск"))
            stationRepo.save(Station(station = "Сочи"))
        }
    }
}