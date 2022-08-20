package com.kursor.chroniclesofww2.viewModels.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.domain.connection.IHost
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import kotlinx.coroutines.launch

class JoinLocalGameViewModel(val localClient: LocalClient) : ViewModel() {


    private val _stateLiveData = MutableLiveData<Pair<Status, Any?>>()

    lateinit var connection: LocalConnection
    var isAccepted = false

    fun connectTo(host: IHost) {
        viewModelScope.launch {
            localClient.connectTo(host = host)
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
                        _stateLiveData.value = Status.ACCEPTED to null
                        // buildMessageWaitingForAccepted()
                    }
                    Const.connection.REJECTED -> _stateLiveData.value = Status.REJECTED to null
//                        Toast.makeText(
//                        activity,
//                        R.string.connection_refused,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    else -> {
                        Log.i("Client", "Default branch")

                        if (!isAccepted) return@collect
                        _stateLiveData.value = Status.GAME_DATA_OBTAINED to string
//                            if (Scenario.fromJson(string) == null) {
//                                Log.i("Client", "Invalid Json")
//                                Tools.currentConnection!!.send(INVALID_JSON)
//                                return
//                            }
//                            val intent = Intent(activity, GameActivity::class.java)
//                            intent.putExtra(Const.connection.CONNECTED_DEVICE, host)
//                                .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.CLIENT)
//                                .putExtra(Const.game.BATTLE, string)
//                            localClient.stopDiscovery()
//                            startActivity(intent)

                    }
                }
            }
        }
    }

    enum class Status {
        ACCEPTED, REJECTED, GAME_DATA_OBTAINED,
    }

}