package com.example.authservice.repositories

import com.example.authservice.dao.UserObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserObject, Long> {
    fun existsUserObjectByEmailAndNickname(
        @Param("email") email: String,
        @Param("nickname") nickname: String
    ) : Boolean

    fun existsUserObjectByEmailAndHashedPassword(
        @Param("email") email : String,
        @Param("password") password : String
    ) : Boolean

    fun getUserObjectByEmailAndHashedPassword(
        @Param("email") email : String,
        @Param("password") password : String
    ) : UserObject

    fun existsUserObjectByEmail(
        @Param("email") email : String
    ) : Boolean

    fun existsUserObjectByNickname(
        @Param("nickname") nickname : String
    ) : Boolean
}