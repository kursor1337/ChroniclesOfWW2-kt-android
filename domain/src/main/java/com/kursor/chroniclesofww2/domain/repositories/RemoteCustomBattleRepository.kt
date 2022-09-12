package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle

interface RemoteCustomBattleRepository : BattleRepository {

    suspend fun getAllBattles(token: String): List<Battle>
    suspend fun getBattleById(id: Int, token: String): Battle?
    suspend fun getMyBattles(token: String): List<Battle>
    suspend fun saveBattle(
        token: String,
        saveBattleReceiveDTO: SaveBattleReceiveDTO
    ): SaveBattleResponseDTO

    suspend fun editBattle(
        token: String,
        editBattleReceiveDTO: EditBattleReceiveDTO
    ): EditBattleResponseDTO

    suspend fun deleteBattle(
        token: String,
        deleteBattleReceiveDTO: DeleteBattleReceiveDTO
    ): DeleteBattleResponseDTO
}