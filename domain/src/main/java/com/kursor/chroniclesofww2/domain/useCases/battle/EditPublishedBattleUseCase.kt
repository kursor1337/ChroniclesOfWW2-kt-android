package com.kursor.chroniclesofww2.domain.useCases.battle

import com.kursor.chroniclesofww2.domain.Moshi
import com.kursor.chroniclesofww2.domain.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.features.EditBattleReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditPublishedBattleUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(battle: Battle) = kotlin.runCatching {
        withContext(Dispatchers.IO) {
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

}