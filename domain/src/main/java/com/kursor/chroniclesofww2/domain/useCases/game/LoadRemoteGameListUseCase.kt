package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteGameRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO

class LoadRemoteGameListUseCase(
    val gameRepository: RemoteGameRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(): RequestResult<List<WaitingGameInfoDTO>> = tryRequest {
        val token = accountRepository.token
        if (token != null) gameRepository.getWaitingGamesList(token)
        else throw UnauthorizedException()
    }

}