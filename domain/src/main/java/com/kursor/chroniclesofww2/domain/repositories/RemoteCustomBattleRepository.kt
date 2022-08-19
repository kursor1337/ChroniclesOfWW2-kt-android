package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle

interface RemoteCustomBattleRepository : BattleRepository {

    suspend fun getAllBattles(): List<Battle>

    suspend fun getBattleById(id: Int): Battle?

    suspend fun getMyBattles(): List<Battle>

    suspend fun saveBattle(
        saveBattleReceiveDTO: SaveBattleReceiveDTO
    ): SaveBattleResponseDTO

    suspend fun editBattle(
        editBattleReceiveDTO: EditBattleReceiveDTO
    ): EditBattleResponseDTO

    suspend fun deleteBattle(
        deleteBattleReceiveDTO: DeleteBattleReceiveDTO
    ): DeleteBattleResponseDTO


}