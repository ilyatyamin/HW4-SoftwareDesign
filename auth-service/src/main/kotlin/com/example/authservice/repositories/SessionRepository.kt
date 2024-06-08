package com.example.authservice.repositories

import com.example.authservice.dao.SessionObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface SessionRepository : JpaRepository<SessionObject, Long> {
    fun existsSessionObjectByUserId(@Param("user_id") userId : Long) : Boolean
}