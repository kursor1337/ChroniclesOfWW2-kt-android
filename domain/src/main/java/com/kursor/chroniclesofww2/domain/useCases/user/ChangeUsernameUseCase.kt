package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.ISettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.ITokenHandler
import com.kursor.chroniclesofww2.domain.interfaces.IUserRepository
import com.kursor.chroniclesofww2.features.UpdateUserInfoReceiveDTO
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeUsernameUseCase(
    val userRepository: IUserRepository,
    val tokenHandler: ITokenHandler,
    val settingsRepository: ISettingsRepository
) {


    operator fun invoke(newUsername: String) {
        settingsRepository.username = newUsername
        if (tokenHandler.token == null) return
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.updateUserInfo(
                token = tokenHandler.token ?: return@launch,
                UpdateUserInfoReceiveDTO(UserInfo(newUsername))
            )
        }
    }


}