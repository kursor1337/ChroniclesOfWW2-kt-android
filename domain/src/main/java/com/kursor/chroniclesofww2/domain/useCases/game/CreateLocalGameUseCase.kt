package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository

class CreateLocalGameUseCase(
    val localServer: LocalServer,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke() {
        localServer.startListening(accountRepository.username)
    }


}