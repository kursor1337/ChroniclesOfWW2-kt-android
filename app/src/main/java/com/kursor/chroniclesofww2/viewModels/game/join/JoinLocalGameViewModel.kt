package com.kursor.chroniclesofww2.viewModels.game.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.game.JoinGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Const.connection.INVALID_JSON
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import kotlinx.coroutines.launch

class JoinLocalGameViewModel(
    private val localClient: LocalClient
) : ViewModel() {


    private val _stateLiveData = MutableLiveData<Pair<JoinGameStatus, Any?>>()
    val stateLiveData: LiveData<Pair<JoinGameStatus, Any?>> get() = _stateLiveData

    lateinit var connection: LocalConnection
    var isAccepted = false

    fun connectTo(host: Host) {
        viewModelScope.launch {
            localClient.connectTo(host = host)
        }
    }

    fun cancelConnection() {
        if (!::connection.isInitialized) return
        viewModelScope.launch {
            connection.send(CANCEL_CONNECTION)
            connection.disconnect()
        }
    }

    fun onConnectionInit() {
        connection = Tools.currentConnection as LocalConnection
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                when (string) {
                    Const.connection.ACCEPTED -> {
                        Log.i("Client", Const.connection.ACCEPTED)
                        isAccepted = true
                        connection.send(Const.connection.REQUEST_GAME_DATA)
                        Log.i("Client", Const.connection.REQUEST_GAME_DATA)
                        _stateLiveData.value = JoinGameStatus.ACCEPTED to null
                    }
                    Const.connection.REJECTED -> _stateLiveData.value =
                        JoinGameStatus.REJECTED to null
                    else -> {

                        Log.i("Client", "Default branch")
                        if (!isAccepted) return@collect
                        if (Moshi.GAMEDATA_ADAPTER.fromJson(string) == null) {
                            connection.send(INVALID_JSON)
                            return@collect
                        }
                        _stateLiveData.value = JoinGameStatus.GAME_DATA_OBTAINED to string
                    }
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            cancelConnection()
        }
    }
}