package com.example.authservice.repositories

import com.example.authservice.dao.UserObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<UserObject, Long> {
    fun existsUserObjectByEmailAndNickname(
        @Param("email") email: String,
        @Param("nickname") nickname: String
    ) : Boolean
}