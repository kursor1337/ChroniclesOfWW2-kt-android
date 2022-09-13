package com.kursor.chroniclesofww2.viewModels.game.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.game.CreateGameStatus
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateLocalGameViewModel(
    val localServer: LocalServer,
    private val accountRepository: AccountRepository
) : ViewModel() {

    lateinit var connection: LocalConnection
    lateinit var gameData: GameData
    private var hostAccepted = false
    private var gameDataContainer: GameDataViewModel.DataContainer? = null
    private var timerStarted = false

    private val _statusLiveData = MutableLiveData<Pair<CreateGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<CreateGameStatus, Any?>> get() = _statusLiveData

    private val serverListener = object : LocalServer.Listener {
        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection
            onConnectionInit()
        }

        override fun onListeningStartError(e: Exception) {
            Log.d(TAG, "onListeningStartError:")
            e.printStackTrace()
            _statusLiveData.postValue(CreateGameStatus.ERROR to null)
        }

        override fun onStartedListening() {
            _statusLiveData.postValue(CreateGameStatus.CREATED to null)
            startTimeoutTimer()
        }

        override fun onFail() {
            _statusLiveData.postValue(CreateGameStatus.ERROR to null)
        }
    }

    init {
        localServer.listener = serverListener
    }

    fun createGame(gameDataContainer: GameDataViewModel.DataContainer) {
        this.gameDataContainer = gameDataContainer
        viewModelScope.launch {
            localServer.startListening(accountRepository.username)
        }
    }

    fun uncreateGame() {
        viewModelScope.launch {
            localServer.stopListening()
            _statusLiveData.value = CreateGameStatus.UNCREATED to null
            timerStarted = false
        }
    }


    fun onConnectionInit() {
        connection = Tools.currentConnection as LocalConnection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    Const.connection.REQUEST_FOR_ACCEPT -> {
                        _statusLiveData.value =
                            CreateGameStatus.REQUEST_FOR_ACCEPT to connection.host
                    }
                    Const.connection.REQUEST_GAME_DATA -> {
                        if (!hostAccepted) return@collect
                        val nonNullGameDataContainer = gameDataContainer!!
                        gameDataContainer = GameDataViewModel.DataContainer(
                            myName = nonNullGameDataContainer.myName,
                            enemyName = connection.host.name,
                            battle = nonNullGameDataContainer.battle,
                            boardHeight = nonNullGameDataContainer.boardHeight,
                            boardWidth = nonNullGameDataContainer.boardWidth,
                            invertNations = nonNullGameDataContainer.invertNations,
                            meInitiator = nonNullGameDataContainer.meInitiator
                        )

                        gameData = gameDataContainer!!
                            .createGameData()!!
                            .getVersionForAnotherPlayer()

                        Log.i("Server", "Client sent ${Const.connection.REQUEST_GAME_DATA}")
                        val gameDataJson = Moshi.GAMEDATA_ADAPTER.toJson(gameData)
                        connection.send(gameDataJson)
                        _statusLiveData.value = CreateGameStatus.GAME_DATA_REQUEST to null
                    }
                    Const.connection.CANCEL_CONNECTION -> {
                        Log.i("Server", Const.connection.CANCEL_CONNECTION)
                        connection.shutdown()
                        Log.i("Server", "Sent invalid json")
                        _statusLiveData.value = CreateGameStatus.CANCEL_CONNECTION to null
                    }
                    Const.connection.INVALID_JSON -> Log.i("Server", "Sent invalid json")
                    else -> {
                        Log.i("CreateLocalGameViewModel", string)
                    }
                }
            }
        }
    }

    fun verdict(string: String) {
        when (string) {
            ACCEPTED -> hostAccepted = true
            REJECTED -> hostAccepted = false
        }
        viewModelScope.launch {
            connection.send(string)
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            if (::connection.isInitialized) connection.disconnect()
            localServer.stopListening()
        }
    }

    fun startTimeoutTimer() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timerStarted = true
                delay(TIMEOUT)
                if (timerStarted) localServer.stopListening()
            }
            if (timerStarted) withContext(Dispatchers.Main) {
                _statusLiveData.value = CreateGameStatus.TIMEOUT to null
            }
        }
    }

    companion object {
        const val TIMEOUT = 300000L
        const val TAG = "CreateLocalGameViewModel"
    }

}