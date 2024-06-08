package com.example.authservice.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "\"user\"")
data class UserObject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var hashedPassword: String,

    @Column(name = "created", columnDefinition = "TIMESTAMP")
    var created: LocalDateTime
);