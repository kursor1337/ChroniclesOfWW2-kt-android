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


    private val _stateLiveData = MutableLiveData<Pair<Status, Any?>>()
    val stateLiveData: LiveData<Pair<Status, Any?>> get() = _stateLiveData

    var createdGameId = 0

    var password = ""
    var responseReceived = false

    private lateinit var connection: RemoteConnection

    fun createGame(gameDataViewModel: GameDataViewModel) {
        viewModelScope.launch {
            connection = RemoteConnection(
                baseUrl = Const.connection.FULL_SERVER_URL,
                path = Routes.Game.CREATE.node,
                httpClient = httpClient,
                dispatcher = Dispatchers.IO
            )
            connection.init()
            connection.send(
                Json.encodeToString(
                    CreateGameReceiveDTO(
                        initiatorLogin = accountRepository.login ?: return@launch,
                        password = password,
                        battle = gameDataViewModel.battleLiveData.value!!,
                        boardWidth = gameDataViewModel.boardWidth,
                        boardHeight = gameDataViewModel.boardHeight,
                        invertNations = gameDataViewModel.invertNations
                    )
                )
            )
        }
        onConnectionInit()
    }


    fun onConnectionInit() {
        Tools.currentConnection = connection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                if (!responseReceived) {
                    val createGameResponseDTO = Json.decodeFromString<CreateGameResponseDTO>(string)
                    createdGameId = createGameResponseDTO.gameId
                    _stateLiveData.value = Status.CREATED to createdGameId
                    return@collect
                }
                when {
                    string == GameFeaturesMessages.GAME_STARTED -> {
                        _stateLiveData.value = Status.CONNECTED to null
                    }
                    string == GameFeaturesMessages.SESSION_TIMED_OUT -> {
                        _stateLiveData.value = Status.TIMEOUT to null
                    }
                    string.startsWith(GameFeaturesMessages.REQUEST_FOR_ACCEPT) -> {
                        _stateLiveData.value = Status.REQUEST_FOR_ACCEPT to string.removePrefix(GameFeaturesMessages.REQUEST_FOR_ACCEPT)
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

    enum class Status {
        CREATED, CONNECTED, TIMEOUT, REQUEST_FOR_ACCEPT
    }

}