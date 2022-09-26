package com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote

import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.features.DeleteBattleReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnpublishBattleUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(battleId: Int) = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            val token = accountRepository.token ?: throw UnauthorizedException()
            remoteCustomBattleRepository.deleteBattle(
                token = token,
                deleteBattleReceiveDTO = DeleteBattleReceiveDTO(id = battleId)
            )

        }
    }

    suspend operator fun invoke(battle: Battle) = invoke(battle.id)

}