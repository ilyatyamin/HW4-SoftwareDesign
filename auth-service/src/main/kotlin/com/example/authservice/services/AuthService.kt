package com.example.authservice.services

import com.example.authservice.dao.UserObject
import com.example.authservice.dto.RegistrationRequest
import com.example.authservice.dto.RegistrationResponse
import com.example.authservice.dto.StatusInfo
import com.example.authservice.repositories.AuthRepository
import com.example.authservice.utils.DataEncryptor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    val authRepository: AuthRepository,
    val encryptor: DataEncryptor
) {
    fun registerUser(request: RegistrationRequest): StatusInfo<RegistrationResponse> {
        // Проверим данные на корректность. Сначала на nullable
        if (request.email == null) {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = RegistrationResponse("Provided email is null.")
            )
        } else if (request.nickname == null) {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = RegistrationResponse("Provided nickname is null.")
            )
        } else if (request.password == null) {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = RegistrationResponse("Provided password is null.")
            )
        }

        // Проверим почту на корректность
        if (!("@" in request.email!! && "." in request.email!! && request.email!!.length >= 5)) {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = RegistrationResponse("Provided email is not correct: email must contains @ and . and be at least 5 symbols in size.")
            )
        }

        // Проверим пароль на корректность
        if (!(request.password!!.length >= 8
                    && latinUpperSymbols.any { it in request.password!! }
                    && latinLowerSymbols.any { it in request.password!! }
                    && numbers.any { it in request.password!! }
                    && specialSymbols.any { it in request.password!! })
        ) {
            return StatusInfo(
                status = HttpStatus.BAD_REQUEST,
                info = RegistrationResponse("Provided password must have size at least 8 symbols, contains upper, lower latin symbols, numbers and special symbols (!@#\$%^&*.,).")
            )
        }

        val potentialUser = UserObject(
            nickname = request.nickname!!,
            email = request.email!!,
            hashedPassword = encryptor.encryptThis(request.password!!),
            created = LocalDateTime.now()
        ) // TODO: зашифровать пароль

        // Проверим, что такого пользователя ранее не существовало в БД
        if (authRepository.existsUserObjectByEmailAndNickname(potentialUser.email, potentialUser.nickname)) {
            return StatusInfo(
                status = HttpStatus.CONFLICT,
                info = RegistrationResponse("User with this email and nickname is existed.")
            )
        }

        // Прошли все проверки, ура
        authRepository.save(potentialUser)
        return StatusInfo(
            status = HttpStatus.OK,
            info = RegistrationResponse("User was registered successfully")
        )

    }


    private val latinUpperSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val latinLowerSymbols = "abcdefghijklmnopqrstuvwxyz"
    private val numbers = "0123456789"
    private val specialSymbols = "!@#$%^&*.,"
}