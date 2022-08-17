package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.SettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.TokenHandler
import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.features.UpdateUserInfoReceiveDTO
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeUsernameUseCase(
    val userRepository: UserRepository,
    val tokenHandler: TokenHandler,
    val settingsRepository: SettingsRepository
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