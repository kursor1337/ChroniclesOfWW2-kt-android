package com.kursor.chroniclesofww2.viewModels.features.game.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.remote.RemoteConnection
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.game.LoadRemoteGameListUseCase
import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.game.JoinGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JoinRemoteGameViewModel(
    private val accountRepository: AccountRepository,
    private val httpClient: HttpClient,
    private val loadRemoteGameListUseCase: LoadRemoteGameListUseCase
) : ViewModel() {

    lateinit var connection: RemoteConnection

    private val _statusLiveData = MutableLiveData<Pair<JoinGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<JoinGameStatus, Any?>> get() = _statusLiveData

    private val _waitingGamesListLiveData = MutableLiveData<List<WaitingGameInfoDTO>>()
    val waitingGamesListLiveData: LiveData<List<WaitingGameInfoDTO>> get() = _waitingGamesListLiveData

    private var waitingGamesList: List<WaitingGameInfoDTO> = emptyList()

    var password = ""
    var isAccepted = false

    fun obtainGameList() {
        viewModelScope.launch {
            loadRemoteGameListUseCase()
                .onSuccess {
                    waitingGamesList = it
                    Log.d(TAG, "obtainGameList: success $waitingGamesList")
                }.onUnauthorized {
                    _statusLiveData.postValue(JoinGameStatus.UNAUTHORIZED to null)
                    return@launch
                }
            _statusLiveData.postValue(JoinGameStatus.GAME_LIST_OBTAINED to waitingGamesList)
            _waitingGamesListLiveData.postValue(waitingGamesList)
        }
    }

    fun search(id: String) {
        _waitingGamesListLiveData.postValue(
            waitingGamesList.filter { it.id.toString().startsWith(id) }
        )
    }

    var gameId = 0

    fun joinGame(id: Int) {
        gameId = id
        val token = accountRepository.token ?: return
        viewModelScope.launch {
            kotlin.runCatching {
                connection = RemoteConnection(
                    fullUrl = Routes.Game.JOIN.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
                    httpClient = httpClient,
                    dispatcher = Dispatchers.IO
                )
                connection.init(token)
                connection.send(
                    Json.encodeToString(
                        JoinGameReceiveDTO(
                            accountRepository.login ?: return@launch,
                            id, password
                        )
                    )
                )
                onConnectionInit()
            }.onFailure {
                _statusLiveData.postValue(JoinGameStatus.UNAUTHORIZED to null)
            }
        }
    }

    fun onConnectionInit() {
        Log.d(TAG, "onConnectionInit: ")
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                Log.d(TAG, "onConnectionInit: collect: $string")
                when (string) {
                    GameFeaturesMessages.ACCEPTED -> {
                        isAccepted = true
                        _statusLiveData.postValue(JoinGameStatus.ACCEPTED to null)
                    }
                    GameFeaturesMessages.REJECTED -> {
                        _statusLiveData.postValue(JoinGameStatus.REJECTED to null)
                    }
                    else -> {
                        val joinGameResponseDTO = Json.decodeFromString<JoinGameResponseDTO>(string)
                        Log.d(TAG, "$joinGameResponseDTO")
                        if (joinGameResponseDTO.message != GameFeaturesMessages.ACCEPTED) return@collect
                        val gameData = joinGameResponseDTO.gameData
                        if (gameData == null) {
                            connection.send(GameFeaturesMessages.INVALID_JSON)
                            return@collect
                        }
                        initSession()
                        _statusLiveData.postValue(
                            JoinGameStatus.GAME_DATA_OBTAINED to Json.encodeToString(
                                gameData
                            )
                        )
                    }
                }
            }
        }
    }

    suspend fun initSession() {
        val token = accountRepository.token ?: return
        connection.shutdown()
        Tools.currentConnection = RemoteConnection(
            fullUrl = Routes.Game.SESSION.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
            httpClient = httpClient,
            dispatcher = Dispatchers.IO
        ).apply {
            init(token)
            send(gameId.toString())
        }
    }

    companion object {
        const val TAG = "JoinRemoteGameViewModel"
    }

}