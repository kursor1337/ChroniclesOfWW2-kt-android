package com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote

import com.kursor.chroniclesofww2.domain.Moshi
import com.kursor.chroniclesofww2.domain.UnauthorizedException
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.features.SaveBattleReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PublishBattleUseCase(
    val remoteCustomBattleRepository: RemoteCustomBattleRepository,
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(
        battleName: String,
        battleDescription: String,
        battleData: Battle.Data
    ) = kotlin.runCatching {
        withContext(Dispatchers.IO) {
            val token = accountRepository.token ?: throw UnauthorizedException()
            val login = accountRepository.login ?: throw UnauthorizedException()

            remoteCustomBattleRepository.saveBattle(
                token = token,
                saveBattleReceiveDTO = SaveBattleReceiveDTO(
                    loginOfCreator = login,
                    name = battleName,
                    description = battleDescription,
                    dataJson = Moshi.BATTLE_DATA_ADAPTER.toJson(battleData)
                )
            )
        }
    }

}