package com.kursor.chroniclesofww2.domain.useCases.auth

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshTokenUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke() = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            accountRepository.refreshToken()
        }
    }

}