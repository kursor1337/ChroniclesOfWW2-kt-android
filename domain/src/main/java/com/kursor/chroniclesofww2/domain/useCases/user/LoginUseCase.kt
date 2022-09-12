package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(
    val accountRepository: AccountRepository,
    val userRepository: UserRepository
) {

    suspend operator fun invoke(loginReceiveDTO: LoginReceiveDTO): Result<LoginResponseDTO> =
        runCatching {
            val loginResponseDTO = withContext(Dispatchers.IO) {
                userRepository.login(loginReceiveDTO)
            }
            withContext(Dispatchers.Main) {
                if (loginResponseDTO.token != null) {
                    accountRepository.token = loginResponseDTO.token
                    accountRepository.login = loginReceiveDTO.login
                    accountRepository.password = loginReceiveDTO.password
                }
                loginResponseDTO
            }
        }


}