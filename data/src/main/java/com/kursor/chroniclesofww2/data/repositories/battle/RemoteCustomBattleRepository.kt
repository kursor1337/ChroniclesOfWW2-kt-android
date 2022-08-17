package com.kursor.chroniclesofww2.data.repositories.battle

import com.kursor.chroniclesofww2.domain.interfaces.RemoteBattleRepository
import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteCustomBattleRepository(val serverUrl: String, val httpClient: HttpClient) :
    BattleRepository, RemoteBattleRepository {

    override val PREFIX = 1_000_000_000
    override val battleList: List<Battle>
        get() = previousRetrievedBattleList

    private var previousRetrievedBattleList: List<Battle> = emptyList()

    override fun findBattleById(id: Int): Battle? {
        val index = id - PREFIX
        return battleList.getOrNull(index)
    }

    suspend fun fetch(token: String) {
        previousRetrievedBattleList = getAllBattles(token)
    }

    override suspend fun getAllBattles(token: String): List<Battle> {
        val response = httpClient.get(Routes.Battles.GET_ALL.absolutePath(serverUrl)) {
            bearerAuth(token)
        }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> emptyList()
            else -> response.body()
        }
    }

    override suspend fun getBattleById(token: String, id: Int): Battle? {
        val response = httpClient.get(Routes.Battles.GET_BY_ID(id).absolutePath(serverUrl)) {
            bearerAuth(token)
        }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> null
            HttpStatusCode.NotFound -> null
            else -> response.body()
        }
    }

    override suspend fun getMyBattles(token: String): List<Battle> {
        val response = httpClient.get(Routes.Battles.MY.absolutePath(serverUrl)) {
            bearerAuth(token)
        }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> emptyList()
            else -> response.body()
        }
    }

    override suspend fun saveBattle(
        token: String,
        saveBattleReceiveDTO: SaveBattleReceiveDTO
    ): SaveBattleResponseDTO {
        val response = httpClient.post(Routes.Battles.SAVE.absolutePath(serverUrl)) {
            bearerAuth(token)
            setBody(saveBattleReceiveDTO)
        }
        return response.body()
    }

    override suspend fun editBattle(
        token: String,
        editBattleReceiveDTO: EditBattleReceiveDTO
    ): EditBattleResponseDTO {
        val response = httpClient.put(Routes.Battles.UPDATE.absolutePath(serverUrl)) {
            bearerAuth(token)
            setBody(editBattleReceiveDTO)
        }
        return response.body()
    }

    override suspend fun deleteBattle(
        token: String,
        deleteBattleReceiveDTO: DeleteBattleReceiveDTO
    ): DeleteBattleResponseDTO {
        val response = httpClient.delete(Routes.Battles.DELETE.absolutePath(serverUrl)) {
            bearerAuth(token)
            setBody(deleteBattleReceiveDTO)
        }
        return response.body()
    }
}