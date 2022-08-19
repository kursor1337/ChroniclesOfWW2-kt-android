package com.kursor.chroniclesofww2

import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.game.moves.Move
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GameSession(
    val connection: Connection,
    val coroutineScope: CoroutineScope
) {

    fun sendMove(move: Move) {
        coroutineScope.launch {
            connection.send(
                Json.encodeToString(
                    GameSessionDTO()
                )
            )
        }
    }


}