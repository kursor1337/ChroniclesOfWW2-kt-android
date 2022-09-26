package com.kursor.chroniclesofww2.domain.useCases.auth

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke() = tryRequest {
        accountRepository.auth()
    }
}
