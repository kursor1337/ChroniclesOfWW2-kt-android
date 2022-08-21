package com.kursor.chroniclesofww2.viewModels.game.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.remote.RemoteConnection
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.game.LoadRemoteGameListUseCase
import com.kursor.chroniclesofww2.features.GameFeaturesMessages
import com.kursor.chroniclesofww2.features.JoinGameReceiveDTO
import com.kursor.chroniclesofww2.features.Routes
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JoinRemoteGameViewModel(
    val accountRepository: AccountRepository,
    val httpClient: HttpClient,
    val loadRemoteGameListUseCase: LoadRemoteGameListUseCase
) : ViewModel() {


    lateinit var connection: RemoteConnection

    private val _stateLiveData = MutableLiveData<Pair<Status, Any?>>()
    val stateLiveData: LiveData<Pair<Status, Any?>> get() = _stateLiveData

    var password = ""
    var isAccepted = false

    fun obtainGameList() {
        viewModelScope.launch {
            _stateLiveData.value = Status.GAME_LIST_OBTAINED to loadRemoteGameListUseCase()
        }
    }

    var gameId = 0

    fun joinGame(id: Int) {
        gameId = id
        viewModelScope.launch {
            connection = RemoteConnection(
                baseUrl = Const.connection.HTTP_SERVER_URL,
                path = Routes.Game.JOIN.node,
                httpClient = httpClient,
                dispatcher = Dispatchers.IO,
                accountRepository.token ?: return@launch
            )
            connection.init()
            connection.send(
                Json.encodeToString(
                    JoinGameReceiveDTO(
                        accountRepository.login ?: return@launch,
                        id, password
                    )
                )
            )
        }
    }

    fun onConnectionInit() {
        Tools.currentConnection = connection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    GameFeaturesMessages.ACCEPTED -> {
                        isAccepted = true
                        _stateLiveData.value = Status.ACCEPTED to null
                    }
                    GameFeaturesMessages.REJECTED -> {
                        _stateLiveData.value = Status.REJECTED to null
                    }
                    else -> {
                        if (!isAccepted) return@collect
                        if (Moshi.GAMEDATA_ADAPTER.fromJson(string) == null) {
                            connection.send(GameFeaturesMessages.INVALID_JSON)
                            return@collect
                        }
                        _stateLiveData.value = Status.GAME_DATA_OBTAINED to string
                        connection.shutdown()
                        Tools.currentConnection = RemoteConnection(
                            baseUrl = Const.connection.HTTP_SERVER_URL,
                            path = Routes.Game.SESSION.node,
                            httpClient = httpClient,
                            dispatcher = Dispatchers.IO,
                            token = accountRepository.token ?: return@collect
                        ).apply {
                            send(gameId.toString())
                        }
                    }
                }
            }
        }
    }


    enum class Status {
        ACCEPTED, REJECTED, GAME_DATA_OBTAINED, GAME_LIST_OBTAINED
    }

}