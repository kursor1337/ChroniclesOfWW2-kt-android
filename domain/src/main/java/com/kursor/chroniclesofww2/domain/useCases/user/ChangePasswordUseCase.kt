package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.SettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.features.ChangePasswordResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCase(
    val userRepository: UserRepository,
    val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(
        token: String,
        newPassword: String
    ): ChangePasswordResponseDTO {
        settingsRepository.password = newPassword
        return withContext(Dispatchers.IO) {
            userRepository.changePassword(token, ChangePasswordReceiveDTO(newPassword))
        }
    }

}