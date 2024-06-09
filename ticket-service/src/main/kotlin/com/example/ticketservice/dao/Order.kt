package com.example.ticketservice.dao

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "order")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId : Int,

    @Column(name = "from_station_id", nullable = false)
    var fromStationId : Int,

    @Column(name = "to_station_id", nullable = false)
    var toStationId : Int,

    @Column(name = "status", nullable = false)
    var status : Int,

    @Column(name = "created", nullable = false, columnDefinition = "TIMESTAMP")
    var created : LocalDateTime,

    @OneToOne
    @JoinColumn(name = "from_station_id", insertable = false, updatable = false)
    val fromStation: Station,

    @OneToOne
    @JoinColumn(name = "to_station_id", insertable = false, updatable = false)
    val toStation: Station
)
