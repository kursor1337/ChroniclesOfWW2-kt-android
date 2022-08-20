package com.kursor.chroniclesofww2.viewModels.game

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.game.CreateLocalGameUseCase
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import kotlinx.coroutines.launch

class CreateLocalGameViewModel(
    val localServer: LocalServer,
    val accountRepository: AccountRepository
) : ViewModel() {

    val createLocalGameUseCase = CreateLocalGameUseCase(localServer, accountRepository)

    fun createGame() {
        viewModelScope.launch {
            createLocalGameUseCase()
        }
    }

    private val _stateLiveData = MutableLiveData<Pair<Status, Any?>>()
    val stateLiveData: LiveData<Pair<Status, Any?>> get() = _stateLiveData

    protected val receiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                Const.connection.REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    if (localServer.password != null) {
                        connection.send(Const.connection.HOST_IS_WITH_PASSWORD)
                    } else {
                        currentDialog?.dismiss()
                        buildMessageConnectionRequest(
                            ,

                        )
                    }
                }
                Const.connection.REQUEST_GAME_DATA -> {
                    Log.i("Server", "Client sent ${Const.connection.REQUEST_GAME_DATA}")
                    connection.send(gameDataJson)
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.connection.CONNECTED_DEVICE, connection.host)
                        .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.GAME_DATA, gameDataJson)
                    Tools.currentConnection = connection
                    localServer.stopListening()
                    startActivity(intent)
                }
                Const.connection.CANCEL_CONNECTION -> {
                    Log.i("Server", Const.connection.CANCEL_CONNECTION)
                    connection.shutdown()
                    Log.i("Server", "Sent invalid json")
                }
                Const.connection.INVALID_JSON -> Log.i("Server", "Sent invalid json")
                else -> {
                    if (string.startsWith(Const.connection.PASSWORD)) {
                        val password = string.removePrefix(Const.connection.PASSWORD)
                        if (password == localServer.password) {
                            currentDialog?.dismiss()
                            buildMessageConnectionRequest(connection.host)
                        }
                    }
                }
            }
        }
    }

    fun onConnectionInit() {

    }

    enum class Status {
        CREATED, CONNECTION_REQUEST, GAME_DATA_REQUEST,
    }

}