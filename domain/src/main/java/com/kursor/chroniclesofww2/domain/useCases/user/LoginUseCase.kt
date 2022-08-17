package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.TokenHandler
import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(val tokenHandler: TokenHandler, val userRepository: UserRepository) {

    suspend operator fun invoke(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO {
        return withContext(Dispatchers.IO) {
            val loginResponseDTO = userRepository.login(loginReceiveDTO)
            if (loginResponseDTO.token != null) tokenHandler.token = loginResponseDTO.token
            loginResponseDTO
        }
    }


}