package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.ISettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.IUserRepository
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.features.ChangePasswordResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCase(
    val userRepository: IUserRepository,
    val settingsRepository: ISettingsRepository
) {

    suspend operator fun invoke(
        token: String,
        changePasswordReceiveDTO: ChangePasswordReceiveDTO
    ): ChangePasswordResponseDTO {
        settingsRepository.password = changePasswordReceiveDTO.newPassword
        return withContext(Dispatchers.IO) {
            userRepository.changePassword(token, changePasswordReceiveDTO)
        }
    }

}