package com.example.authservice.services

import com.example.authservice.dao.SessionObject
import com.example.authservice.dao.UserObject
import com.example.authservice.dto.*
import com.example.authservice.repositories.SessionRepository
import com.example.authservice.repositories.UserRepository
import com.example.authservice.utils.DataEncryptor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class AuthService(
    val userRepository: UserRepository,
    val sessionRepository: SessionRepository,
    val tokenService: JWTService,
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
        )

        // Проверим, что такого пользователя ранее не существовало в БД
        if (userRepository.existsUserObjectByEmailAndNickname(potentialUser.email, potentialUser.nickname)) {
            return StatusInfo(
                status = HttpStatus.CONFLICT,
                info = RegistrationResponse("User with this email and nickname is existed.")
            )
        }

        // Прошли все проверки, ура
        userRepository.save(potentialUser)
        return StatusInfo(
            status = HttpStatus.OK,
            info = RegistrationResponse("User was registered successfully")
        )

    }

    fun tryToLogin(request: AuthRequest): StatusInfo<AuthResponse> {
        // Нужно проверить, существует ли такой пользователь вообще
        val isExisted = userRepository.existsUserObjectByEmailAndHashedPassword(
            email = request.email,
            password = encryptor.encryptThis(request.password)
        )

        if (!isExisted) {
            return StatusInfo(
                status = HttpStatus.FORBIDDEN,
                info = AuthResponse(message = "User with this login and password doesn't exists.")
            )
        }

        // Такой пользователь существует, а значит надо сгенерировать Токен
        val userObject = userRepository.getUserObjectByEmailAndHashedPassword(
            email = request.email,
            password = encryptor.encryptThis(request.password)
        )

        val generatedToken = tokenService.generateToken(userObject)
        val expires = tokenService.getValidDate(generatedToken).toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        // Может такое случиться, что пользователь уже был авторизирован ранее
        if (sessionRepository.existsSessionObjectByUserId(userObject.id!!)) {
            val session = sessionRepository.findById(userObject.id!!)
            session.get().token = generatedToken
            session.get().expires = expires
            sessionRepository.save(session.get())
        } else {
            sessionRepository.save(
                SessionObject(
                    userId = userObject.id!!, token = generatedToken, expires = expires
                )
            )
        }


        return StatusInfo(
            status = HttpStatus.OK,
            info = AuthResponse(
                message = "User was entered successfully.",
                token = generatedToken
            )
        )
    }

    fun getInfo(request : InfoRequest) {

    }


    private val latinUpperSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val latinLowerSymbols = "abcdefghijklmnopqrstuvwxyz"
    private val numbers = "0123456789"
    private val specialSymbols = "!@#$%^&*.,"
}