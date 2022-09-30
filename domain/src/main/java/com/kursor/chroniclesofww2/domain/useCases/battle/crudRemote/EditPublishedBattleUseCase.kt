package com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.domain.tools.Moshi
import com.kursor.chroniclesofww2.domain.tools.UnauthorizedException
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.EditBattleReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle

class EditPublishedBattleUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(battle: Battle) = tryRequest {
        val token = accountRepository.token ?: throw UnauthorizedException()
        remoteCustomBattleRepository.editBattle(
            token = token,
            editBattleReceiveDTO = EditBattleReceiveDTO(
                id = battle.id,
                name = battle.name,
                description = battle.description,
                dataJson = Moshi.BATTLE_DATA_ADAPTER.toJson(battle.data)
            )
        )
    }
}