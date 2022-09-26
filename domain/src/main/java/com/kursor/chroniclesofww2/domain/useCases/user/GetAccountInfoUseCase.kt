package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.AccountInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAccountInfoUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(): RequestResult<AccountInfo> = tryRequest {
        accountRepository.getAccountInfo() ?: throw UnauthorizedException()
    }
}