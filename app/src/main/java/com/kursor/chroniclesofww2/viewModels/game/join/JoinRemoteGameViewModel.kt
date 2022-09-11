package com.kursor.chroniclesofww2.viewModels.game.join

import android.util.Log
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
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import com.kursor.chroniclesofww2.game.JoinGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JoinRemoteGameViewModel(
    private val accountRepository: AccountRepository,
    private val httpClient: HttpClient,
    private val loadRemoteGameListUseCase: LoadRemoteGameListUseCase
) : ViewModel() {

    lateinit var connection: RemoteConnection

    private val _stateLiveData = MutableLiveData<Pair<JoinGameStatus, Any?>>()
    val stateLiveData: LiveData<Pair<JoinGameStatus, Any?>> get() = _stateLiveData

    private val _waitingGamesListLiveData = MutableLiveData<List<WaitingGameInfoDTO>>()
    val waitingGamesListLiveData: LiveData<List<WaitingGameInfoDTO>> get() = _waitingGamesListLiveData

    var password = ""
    var isAccepted = false

    fun obtainGameList() {
        viewModelScope.launch {
            var waitingGamesList: List<WaitingGameInfoDTO> = emptyList()
            loadRemoteGameListUseCase()
                .onSuccess {
                    waitingGamesList = it
                    Log.d("JoinRemoteGameViewModel", "obtainGameList: success ${waitingGamesList}")
                }.onFailure {
                    throw it
                    _stateLiveData.value = JoinGameStatus.UNAUTHORIZED to null
                    return@launch
                }
            _stateLiveData.value = JoinGameStatus.GAME_LIST_OBTAINED to waitingGamesList
            _waitingGamesListLiveData.value = waitingGamesList
        }
    }

    fun search(id: String) {
        _waitingGamesListLiveData.value =
            _waitingGamesListLiveData.value?.filter { it.id.toString().startsWith(id) }
    }

    var gameId = 0

    fun joinGame(id: Int) {
        gameId = id
        viewModelScope.launch {
            connection = RemoteConnection(
                fullUrl = Routes.Game.JOIN.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
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
            onConnectionInit()
        }
    }

    fun onConnectionInit() {
        Tools.currentConnection = connection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    GameFeaturesMessages.ACCEPTED -> {
                        isAccepted = true
                        _stateLiveData.value = JoinGameStatus.ACCEPTED to null
                    }
                    GameFeaturesMessages.REJECTED -> {
                        _stateLiveData.value = JoinGameStatus.REJECTED to null
                    }
                    else -> {
                        if (!isAccepted) return@collect
                        if (Moshi.GAMEDATA_ADAPTER.fromJson(string) == null) {
                            connection.send(GameFeaturesMessages.INVALID_JSON)
                            return@collect
                        }
                        connection.shutdown()
                        Tools.currentConnection = RemoteConnection(
                            fullUrl = Routes.Game.CREATE.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
                            httpClient = httpClient,
                            dispatcher = Dispatchers.IO,
                            token = accountRepository.token ?: return@collect
                        ).apply {
                            send(gameId.toString())
                        }
                        _stateLiveData.value = JoinGameStatus.GAME_DATA_OBTAINED to string
                    }
                }
            }
        }
    }

}