package com.kursor.chroniclesofww2.domain.useCases.battle.load

import com.kursor.chroniclesofww2.domain.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class LoadRemoteCustomBattleListUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): Result<List<Battle>> = runCatching {
        val token = accountRepository.token
        if (token != null) {
            remoteCustomBattleRepository.getAllBattles(token)
        } else {
            throw UnauthorizedException()
        }
    }

}