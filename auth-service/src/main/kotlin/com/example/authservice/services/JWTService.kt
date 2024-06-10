package com.example.authservice.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.authservice.dao.UserObject
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey

@Service
class JWTService {
    fun generateToken(user: UserObject): String {
        return JWT.create().withSubject(user.nickname)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + tokenHours * 60 * 60 * 1000))
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun isValid(token: String, user: UserObject): Boolean {
        val claims = getClaims(token)
        return claims.expiresAt.before(Date(System.currentTimeMillis())) && claims.subject == user.nickname
    }

    fun getValidDate(token : String) : Date {
        return getClaims(token).expiresAt
    }

    private fun getClaims(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    private val tokenHours = 1
    private val secretKey = "rfnQI8sGCRIW7GYFIW7YRhduyYG3PXKDftuUBTffut66Tftr3UV"
}