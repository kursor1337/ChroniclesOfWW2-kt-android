package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.IUserRepository
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(val userRepository: IUserRepository) {

    suspend operator fun invoke(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO {
        return withContext(Dispatchers.IO) {
            userRepository.login(loginReceiveDTO)
        }
    }


}