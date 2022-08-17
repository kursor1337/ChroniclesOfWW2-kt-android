package com.kursor.chroniclesofww2.domain.interfaces

import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle

interface RemoteBattleRepository {

    suspend fun getAllBattles(token: String): List<Battle>

    suspend fun getBattleById(token: String, id: Int): Battle?

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