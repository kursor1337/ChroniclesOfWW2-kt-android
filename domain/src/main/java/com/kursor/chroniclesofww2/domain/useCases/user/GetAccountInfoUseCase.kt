package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAccountInfoUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(): User? {
        withContext(Dispatchers.IO) {

        }
    }

}