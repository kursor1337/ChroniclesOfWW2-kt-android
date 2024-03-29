package com.kursor.chroniclesofww2.viewModels.features.game.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.remote.RemoteConnection
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.features.CreateGameReceiveDTO
import com.kursor.chroniclesofww2.features.CreateGameResponseDTO
import com.kursor.chroniclesofww2.features.GameFeaturesMessages
import com.kursor.chroniclesofww2.features.Routes
import com.kursor.chroniclesofww2.game.CreateGameStatus
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreateRemoteGameViewModel(
    private val accountRepository: AccountRepository,
    private val httpClient: HttpClient
) : ViewModel() {


    private val _statusLiveData = MutableLiveData<Pair<CreateGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<CreateGameStatus, Any?>> get() = _statusLiveData

    var createdGameId = 0

    var password = ""
    var responseReceived = false
    var gameData: GameData? = null

    private lateinit var connection: RemoteConnection

    fun createGame(gameDataContainer: GameDataViewModel.DataContainer) {
        val token = accountRepository.token
        if (token == null) {
            _statusLiveData.postValue(CreateGameStatus.UNAUTHORIZED to null)
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                connection = RemoteConnection(
                    fullUrl = Routes.Game.CREATE.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
                    httpClient = httpClient,
                    dispatcher = Dispatchers.IO
                ).apply {
                    init(token)
                    send(
                        Json.encodeToString(
                            CreateGameReceiveDTO(
                                initiatorLogin = accountRepository.login!!,
                                password = password,
                                battle = gameDataContainer.battle!!,
                                boardWidth = gameDataContainer.boardWidth,
                                boardHeight = gameDataContainer.boardHeight,
                                invertNations = gameDataContainer.invertNations
                            )
                        )
                    )
                }
                onConnectionInit()
            }.onFailure {
                _statusLiveData.postValue(CreateGameStatus.UNAUTHORIZED to null)
            }

        }

    }

    fun onConnectionInit() {
        Tools.currentConnection = connection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                Log.d(TAG, "onConnectionInit: collect: $string")
                try {
                    val createGameResponseDTO = Json.decodeFromString<CreateGameResponseDTO>(string)
                    createdGameId = createGameResponseDTO.gameId
                    _statusLiveData.postValue(CreateGameStatus.CREATED to createdGameId)
                    responseReceived = true
                    return@collect
                } catch (e: SerializationException) {
                    e.printStackTrace()
                }
                try {
                    gameData = Json.decodeFromString(string)
                    connection.shutdown()
                    initSession()
                    _statusLiveData.postValue(
                        CreateGameStatus.GAME_DATA_OBTAINED to Json.encodeToString(
                            gameData
                        )
                    )
                } catch (e: SerializationException) {
                    e.printStackTrace()
                }
                when {
                    string == GameFeaturesMessages.GAME_STARTED -> {
                        _statusLiveData.postValue(CreateGameStatus.GAME_START to null)
                    }
                    string == GameFeaturesMessages.SESSION_TIMED_OUT -> {
                        responseReceived = false
                        _statusLiveData.postValue(CreateGameStatus.TIMEOUT to null)
                    }
                    string.startsWith(GameFeaturesMessages.REQUEST_FOR_ACCEPT) -> {
                        _statusLiveData.postValue(
                            CreateGameStatus.REQUEST_FOR_ACCEPT to string.removePrefix(
                                GameFeaturesMessages.REQUEST_FOR_ACCEPT
                            )
                        )
                    }
                }
            }
        }
    }

    fun verdict(string: String) {
        viewModelScope.launch {
            connection.send(string)
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
            send(createdGameId.toString())
        }

    }

    fun cancelConnection() {
        viewModelScope.launch {
            Tools.currentConnection = null
            connection.send(GameFeaturesMessages.CANCEL_CONNECTION)
            connection.disconnect()
        }
    }

    companion object {
        const val TAG = "CreateRemoteGameViewModel"
    }

}