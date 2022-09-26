package com.kursor.chroniclesofww2.domain.useCases.battle.load

import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadMyRemoteBattlesListUseCase(
    private val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(): RequestResult<List<Battle>> = tryRequest {
        val token = accountRepository.token ?: throw UnauthorizedException()
        remoteCustomBattleRepository.getMyBattles(token)
    }
}