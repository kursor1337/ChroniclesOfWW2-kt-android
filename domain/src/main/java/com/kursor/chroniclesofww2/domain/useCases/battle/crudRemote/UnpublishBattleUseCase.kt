package com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.DeleteBattleReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle

class UnpublishBattleUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(battleId: Int) = tryRequest {
        val token = accountRepository.token ?: throw UnauthorizedException()
        remoteCustomBattleRepository.deleteBattle(
            token = token,
            deleteBattleReceiveDTO = DeleteBattleReceiveDTO(id = battleId)
        )
    }

    suspend operator fun invoke(battle: Battle) = invoke(battle.id)

}