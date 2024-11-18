package com.example.ticketservice.dao

import jakarta.persistence.*

@Entity(name = "station")
data class Station(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int? = null,

    @Column(nullable = false)
    var station : String
)
