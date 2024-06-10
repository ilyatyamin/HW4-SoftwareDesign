package com.example.authservice.dao

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity(name = "session")
data class SessionObject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false, updatable = true)
    var userId : Long,

    @Column(name = "token", nullable = false)
    var token : String,

    @Column(name = "expires", nullable = false, columnDefinition = "TIMESTAMP")
    var expires : LocalDateTime,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user : UserObject? = null
)
