package com.kursor.chroniclesofww2.data.repositories.battle

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.repositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteCustomBattleRepositoryImpl(
    val serverUrl: String,
    val httpClient: HttpClient,
    val accountRepository: AccountRepository
) : RemoteCustomBattleRepository {

    override val PREFIX = 1_000_000_000
    override val battleList: List<Battle>
        get() = previousRetrievedBattleList

    private var previousRetrievedBattleList: List<Battle> = emptyList()

    override fun findBattleById(id: Int): Battle? {
        val index = id - PREFIX
        return battleList.getOrNull(index)
    }

    suspend fun fetch() {
        previousRetrievedBattleList = getAllBattles()
    }

    override suspend fun getAllBattles(): List<Battle> {
        if (accountRepository.token == null) accountRepository.auth()

        val response = httpClient.get(Routes.Battles.GET_ALL.absolutePath(serverUrl)) {
            bearerAuth(accountRepository.token ?: return emptyList())
        }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> {
                accountRepository.auth()
                getAllBattles()
            }
            else -> response.body()
        }
    }

    override suspend fun getBattleById(id: Int): Battle? {
        if (accountRepository.token == null) accountRepository.auth()
        val response =
            httpClient.get(Routes.Battles.GET_BY_ID(id).absolutePath(serverUrl)) {
                bearerAuth(accountRepository.token ?: return null)
            }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> {
                accountRepository.auth()
                getBattleById(id)
            }
            HttpStatusCode.NotFound -> null
            else -> response.body()
        }
    }

    override suspend fun getMyBattles(): List<Battle> {
        if (accountRepository.token == null) accountRepository.auth()

        val response = httpClient.get(Routes.Battles.MY.absolutePath(serverUrl)) {
            bearerAuth(accountRepository.token ?: return emptyList())
        }
        return when (response.status) {
            HttpStatusCode.Unauthorized -> {
                accountRepository.auth()
                getMyBattles()
            }
            else -> response.body()
        }
    }

    override suspend fun saveBattle(
        saveBattleReceiveDTO: SaveBattleReceiveDTO
    ): SaveBattleResponseDTO {
        if (accountRepository.token == null) accountRepository.auth()

        val response = httpClient.post(Routes.Battles.SAVE.absolutePath(serverUrl)) {
            bearerAuth(accountRepository.token ?: "")
            setBody(saveBattleReceiveDTO)
        }
        val saveBattleResponseDTO = response.body<SaveBattleResponseDTO>()
        return if (saveBattleResponseDTO.id == null) {
            accountRepository.auth()
            saveBattle(saveBattleReceiveDTO)
        } else saveBattleResponseDTO
    }

    override suspend fun editBattle(
        editBattleReceiveDTO: EditBattleReceiveDTO
    ): EditBattleResponseDTO {
        accountRepository.auth()

        val response = httpClient.put(Routes.Battles.UPDATE.absolutePath(serverUrl)) {
            bearerAuth(accountRepository.token ?: "")
            setBody(editBattleReceiveDTO)
        }
        return response.body()
    }

    override suspend fun deleteBattle(
        deleteBattleReceiveDTO: DeleteBattleReceiveDTO
    ): DeleteBattleResponseDTO {
        accountRepository.auth()
        val response = httpClient.delete(Routes.Battles.DELETE.absolutePath(serverUrl)) {
            bearerAuth(accountRepository.token ?: "")
            setBody(deleteBattleReceiveDTO)
        }
        return response.body()
    }
}