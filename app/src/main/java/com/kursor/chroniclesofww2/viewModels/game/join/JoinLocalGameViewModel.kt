package com.kursor.chroniclesofww2.viewModels.game.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.domain.connection.Connection
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

    private val _statusLiveData = MutableLiveData<Pair<JoinGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<JoinGameStatus, Any?>> get() = _statusLiveData

    lateinit var connection: LocalConnection
    var isAccepted = false

    private val localClientListener: LocalClient.Listener = object : LocalClient.Listener {
        override fun onException(e: Exception) {
            Log.d(TAG, "onException: ")
            e.printStackTrace()
            _statusLiveData.postValue(JoinGameStatus.ERROR to null)
        }

        override fun onFail(errorCode: Int) {
            Log.d(TAG, "onFail: errorCode = $errorCode")
            _statusLiveData.postValue(JoinGameStatus.ERROR to null)
        }

        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection
            onConnectionInit()
        }
    }

    init {
        localClient.listener = localClientListener
    }

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
                        _statusLiveData.postValue(JoinGameStatus.ACCEPTED to null)
                    }
                    Const.connection.REJECTED -> _statusLiveData.value =
                        JoinGameStatus.REJECTED to null
                    else -> {

                        Log.i("Client", "Default branch")
                        if (!isAccepted) return@collect
                        if (Moshi.GAMEDATA_ADAPTER.fromJson(string) == null) {
                            connection.send(INVALID_JSON)
                            return@collect
                        }
                        _statusLiveData.postValue(JoinGameStatus.GAME_DATA_OBTAINED to string)
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

    companion object {
        const val TAG = "JoinLocalGameViewModel"
    }
}