package com.kursor.chroniclesofww2.viewModels.game.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
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
import io.ktor.util.debug.*
import kotlinx.coroutines.launch

class CreateLocalGameViewModel(
    private val localServer: LocalServer,
    private val accountRepository: AccountRepository
) : ViewModel() {


    lateinit var connection: LocalConnection
    lateinit var gameData: GameData
    var hostAccepted = false
    var gameDataContainer: GameDataViewModel.DataContainer? = null

    fun createGame(gameDataContainer: GameDataViewModel.DataContainer) {
        this.gameDataContainer = gameDataContainer
        viewModelScope.launch {
            localServer.startListening(accountRepository.username)
            _stateLiveData.value = CreateGameStatus.CREATED to null
        }
    }

    fun uncreateGame() {
        viewModelScope.launch {
            localServer.stopListening()
            _stateLiveData.value = CreateGameStatus.UNCREATED to null
        }
    }

    private val _stateLiveData = MutableLiveData<Pair<CreateGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<CreateGameStatus, Any?>> get() = _stateLiveData

    fun onConnectionInit() {
        connection = Tools.currentConnection as LocalConnection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    Const.connection.REQUEST_FOR_ACCEPT -> {
                        _stateLiveData.value =
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
                        _stateLiveData.value = CreateGameStatus.GAME_DATA_REQUEST to null
                    }
                    Const.connection.CANCEL_CONNECTION -> {
                        Log.i("Server", Const.connection.CANCEL_CONNECTION)
                        connection.shutdown()
                        Log.i("Server", "Sent invalid json")
                        _stateLiveData.value = CreateGameStatus.CANCEL_CONNECTION to null
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

}