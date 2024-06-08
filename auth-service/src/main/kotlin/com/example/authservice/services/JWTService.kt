package com.example.authservice.services

import com.example.authservice.dao.UserObject
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey

@Service
class JWTService {
    fun generateToken(user: UserObject): String {
        return Jwts.builder()
            .subject(user.nickname)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + tokenHours * 60 * 60 * 1000))
            .signWith(signedKey)
            .compact()
    }

    fun isValid(token: String, user: UserObject): Boolean {
        val claims = getClaims(token)
        return claims.expiration.before(Date(System.currentTimeMillis())) && claims.subject == user.nickname
    }

    fun getValidDate(token : String) : Date {
        return getClaims(token).expiration
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser().verifyWith(signedKey)
            .build().parseSignedClaims(token).payload
    }

    private val tokenHours = 1
    private val secretKey = "rfnQI8sGCRIW7GYFIW7YRhduyYG3PXKDftuUBTffut66Tftr3UV"
    private val signedKey: SecretKey
        get() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
}