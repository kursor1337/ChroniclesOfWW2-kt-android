package com.kursor.chroniclesofww2.data.repositories.battleRepositories

import com.kursor.chroniclesofww2.domain.interfaces.IRemoteBattleRepository
import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteCustomBattleRepository(val httpClient: HttpClient) : BattleRepository, IRemoteBattleRepository {

    override val PREFIX = 1_000_000_000
    override val battleList: List<Battle>
        get() = previousRetrievedBattleList

    private var previousRetrievedBattleList: List<Battle> = emptyList()

    override fun findBattleById(id: Int): Battle? {
        val index = id - PREFIX
        return battleList.getOrNull(index)
    }

    suspend fun fetch() {

    }

    override suspend fun getAllBattles(token: String): List<Battle> {
        httpClient.get(Routes.Battles.GET_ALL.absolutePath("")) {

        }
    }

    override suspend fun getBattleById(token: String, id: Int): Battle? {

    }

    override suspend fun getMyBattles(token: String): List<Battle> {
        TODO("Not yet implemented")
    }

    override suspend fun saveBattle(
        token: String,
        saveBattleReceiveDTO: SaveBattleReceiveDTO
    ): SaveBattleResponseDTO {
        TODO("Not yet implemented")
    }

    override suspend fun editBattle(
        token: String,
        editBattleReceiveDTO: EditBattleReceiveDTO
    ): EditBattleResponseDTO {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBattle(
        token: String,
        deleteBattleReceiveDTO: DeleteBattleReceiveDTO
    ): DeleteBattleResponseDTO {
        TODO("Not yet implemented")
    }
}