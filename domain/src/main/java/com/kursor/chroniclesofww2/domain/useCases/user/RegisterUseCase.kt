package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.RegisterReceiveDTO
import com.kursor.chroniclesofww2.features.RegisterResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUseCase(
    val accountRepository: AccountRepository,
    val userRepository: UserRepository
) {

    suspend operator fun invoke(registerReceiveDTO: RegisterReceiveDTO): RequestResult<RegisterResponseDTO> =
        tryRequest {
            val registerResponseDTO = withContext(Dispatchers.IO) {
                userRepository.register(registerReceiveDTO)
            }
            withContext(Dispatchers.Main) {
                if (registerResponseDTO.token != null) {
                    accountRepository.token = registerResponseDTO.token
                    accountRepository.login = registerReceiveDTO.login
                    accountRepository.password = registerReceiveDTO.password
                }
                registerResponseDTO
            }
        }
}