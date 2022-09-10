package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.features.UpdateUserInfoReceiveDTO
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeUsernameUseCase(
    val accountRepository: AccountRepository
) {


    operator fun invoke(newUsername: String): Result<Unit> = runCatching {
        accountRepository.username = newUsername
        if (accountRepository.token == null) return@runCatching
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                accountRepository.updateUserInfo(
                    UpdateUserInfoReceiveDTO(UserInfo(newUsername))
                )
            }

        }
    }


}