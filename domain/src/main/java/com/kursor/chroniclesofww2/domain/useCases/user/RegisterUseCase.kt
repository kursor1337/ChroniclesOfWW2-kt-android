package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.IUserService
import com.kursor.chroniclesofww2.features.RegisterReceiveDTO
import com.kursor.chroniclesofww2.features.RegisterResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUseCase(val userService: IUserService) {

    suspend operator fun invoke(registerReceiveDTO: RegisterReceiveDTO): RegisterResponseDTO {
        return withContext(Dispatchers.IO) {
            userService.register(registerReceiveDTO)
        }
    }


}