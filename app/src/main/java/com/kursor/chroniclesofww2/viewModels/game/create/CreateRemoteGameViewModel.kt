package com.kursor.chroniclesofww2.viewModels.game.create

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
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreateRemoteGameViewModel(
    val accountRepository: AccountRepository,
    val httpClient: HttpClient
) : ViewModel() {


    private val _stateLiveData = MutableLiveData<Pair<CreateGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<CreateGameStatus, Any?>> get() = _stateLiveData

    var createdGameId = 0

    var password = ""
    var responseReceived = false

    private lateinit var connection: RemoteConnection

    fun createGame(gameDataViewModel: GameDataViewModel) {
        viewModelScope.launch {
            connection = RemoteConnection(
                fullUrl = Routes.Game.CREATE.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
                httpClient = httpClient,
                dispatcher = Dispatchers.IO,
                token = accountRepository.token!!
            )
            connection.init()
            connection.send(
                Json.encodeToString(
                    CreateGameReceiveDTO(
                        initiatorLogin = accountRepository.login!!,
                        password = password,
                        battle = gameDataViewModel.battleLiveData.value!!,
                        boardWidth = gameDataViewModel.boardWidth,
                        boardHeight = gameDataViewModel.boardHeight,
                        invertNations = gameDataViewModel.invertNations
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
                if (!responseReceived) {
                    val createGameResponseDTO = Json.decodeFromString<CreateGameResponseDTO>(string)
                    createdGameId = createGameResponseDTO.gameId
                    _stateLiveData.value = CreateGameStatus.CREATED to createdGameId
                    responseReceived = true
                    return@collect
                }
                when {
                    string == GameFeaturesMessages.GAME_STARTED -> {
                        _stateLiveData.value = CreateGameStatus.GAME_START to null
                    }
                    string == GameFeaturesMessages.SESSION_TIMED_OUT -> {
                        _stateLiveData.value = CreateGameStatus.TIMEOUT to null
                    }
                    string.startsWith(GameFeaturesMessages.REQUEST_FOR_ACCEPT) -> {
                        _stateLiveData.value =
                            CreateGameStatus.REQUEST_FOR_ACCEPT to string.removePrefix(
                                GameFeaturesMessages.REQUEST_FOR_ACCEPT
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

    fun cancelConnection() {
        viewModelScope.launch {
            Tools.currentConnection = null
            connection.send(GameFeaturesMessages.CANCEL_CONNECTION)
            connection.disconnect()
        }
    }

}