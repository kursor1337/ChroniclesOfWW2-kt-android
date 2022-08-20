package com.kursor.chroniclesofww2.viewModels.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.game.CreateLocalGameUseCase
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import kotlinx.coroutines.launch

class CreateLocalGameViewModel(
    val localServer: LocalServer,
    val accountRepository: AccountRepository
) : ViewModel() {

    val createLocalGameUseCase = CreateLocalGameUseCase(localServer, accountRepository)

    lateinit var connection: LocalConnection
    lateinit var gameData: GameData
    var hostAccepted = false

    fun createGame(gameData: GameData) {
        this.gameData = gameData
        viewModelScope.launch {
            createLocalGameUseCase()
        }
    }

    fun uncreateGame() {
        viewModelScope.launch {
            localServer.stopListening()
        }
    }

    private val _stateLiveData = MutableLiveData<Pair<Status, Any?>>()
    val stateLiveData: LiveData<Pair<Status, Any?>> get() = _stateLiveData

    fun onConnectionInit() {
        connection = Tools.currentConnection as LocalConnection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    Const.connection.REQUEST_FOR_ACCEPT -> {
                        _stateLiveData.value = Status.CONNECTION_REQUEST to connection.host
                    }
                    Const.connection.REQUEST_GAME_DATA -> {
                        if (!hostAccepted) return@collect
                        Log.i("Server", "Client sent ${Const.connection.REQUEST_GAME_DATA}")
                        val gameDataJson = Moshi.GAMEDATA_ADAPTER.toJson(gameData)
                        connection.send(gameDataJson)
                        _stateLiveData.value = Status.GAME_DATA_REQUEST to null
                    }
                    Const.connection.CANCEL_CONNECTION -> {
                        Log.i("Server", Const.connection.CANCEL_CONNECTION)
                        connection.shutdown()
                        Log.i("Server", "Sent invalid json")
                        _stateLiveData.value = Status.CANCEL_CONNECTION to null
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

    enum class Status {
        CREATED, CONNECTION_REQUEST, GAME_DATA_REQUEST, CANCEL_CONNECTION
    }

}