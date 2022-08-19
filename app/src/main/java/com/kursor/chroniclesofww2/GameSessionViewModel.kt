package com.kursor.chroniclesofww2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.features.GameSessionDTO
import com.kursor.chroniclesofww2.features.GameSessionMessageType
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.objects.Const
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameSessionViewModel(
    val connection: Connection = Connection.CURRENT!!
) : ViewModel() {


    private val _lastMoveLiveData = MutableLiveData<Move>()
    val lastMoveLiveData: LiveData<Move> get() = _lastMoveLiveData


    init {
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                val moveSimplified = Move.decodeFromStringToSimplified(string)

            }
        }
    }

    fun sendMove(move: Move) {
        viewModelScope.launch {
            connection.send(
                Json.encodeToString(
                    GameSessionDTO(
                        type = GameSessionMessageType.MOVE,
                        message = move.encodeToString()
                    )
                )
            )
        }
    }


    fun disconnect() {
        viewModelScope.launch {
            connection.send(
                Json.encodeToString(
                    GameSessionDTO(
                        type = GameSessionMessageType.DISCONNECT,
                        message = Const.connection.CANCEL_CONNECTION
                    )
                )
            )
        }
    }




}

