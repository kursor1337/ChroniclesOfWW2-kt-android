package com.kursor.chroniclesofww2.viewModels.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.features.GameSessionDTO
import com.kursor.chroniclesofww2.features.GameSessionMessageType
import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.controllers.TwoHostsController
import com.kursor.chroniclesofww2.model.game.GamePlayer
import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.board.Tile
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.GameActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameSessionViewModel(
    val connection: Connection,
    gameData: GameData
) : ViewModel() {


    private val _lastMoveLiveData = MutableLiveData<Move>()
    val lastMoveLiveData: LiveData<Move> get() = _lastMoveLiveData

    private val _legalMovesLiveData = MutableLiveData<List<Move>>()
    val legalMovesLiveData: LiveData<List<Move>> get() = _legalMovesLiveData

    private val _gameEndLiveData = MutableLiveData<GamePlayer>()
    val gameEndLiveData: LiveData<GamePlayer> get() = _gameEndLiveData

    private val _systemMessagesLiveData = MutableLiveData<String>()
    val systemMessagesLiveData: LiveData<String> get() = _systemMessagesLiveData

    val model = Model(gameData)
    val controller = TwoHostsController(model, object : Controller.Listener {

        override fun onMotionMoveComplete(motionMove: MotionMove, turn: Int) {
            Log.i(GameActivity.TAG, "onMotionMoveComplete: start")
            _legalMovesLiveData.value = emptyList()
            _lastMoveLiveData.value = motionMove
            sendMove(motionMove)
        }

        override fun onAddMoveComplete(addMove: AddMove, turn: Int) {
            Log.i(GameActivity.TAG, "onAddMoveComplete: ")
            _legalMovesLiveData.value = emptyList()
            _lastMoveLiveData.value = addMove
            sendMove(addMove)
        }

        override fun onEnemyMoveComplete(move: Move, turn: Int) {
            Log.i(GameActivity.TAG, "onEnemyMoveComplete: ")
            Log.i("EventListener", "onEnemyMoveComplete")
            _legalMovesLiveData.value = emptyList()
            _lastMoveLiveData.value = move
        }

        override fun onMotionMoveCanceled(i: Int, j: Int) {
            Log.i(GameActivity.TAG, "onMotionMoveCanceled: ")
            _legalMovesLiveData.value = emptyList()
        }

        override fun onAddMoveCanceled() {
            Log.i(GameActivity.TAG, "onAddMoveCanceled: ")
            _legalMovesLiveData.value = emptyList()
        }

        override fun onReserveClicked(reserve: Reserve, possibleMoves: List<AddMove>) {
            Log.i(GameActivity.TAG, "onReserveClicked: ")
            Log.i(
                GameActivity.TAG,
                "onReserveClicked: number of possible moves = ${possibleMoves.size}"
            )
            _legalMovesLiveData.value = possibleMoves
        }

        override fun onTileClicked(tile: Tile, possibleMoves: List<MotionMove>) {
            Log.i(GameActivity.TAG, "onTileClicked: ")
            _legalMovesLiveData.value = possibleMoves
        }

        override fun onGameEnd(meWon: Boolean) {
            Log.i(GameActivity.TAG, "onGameEnd: ")
            _gameEndLiveData.value = if (meWon) model.me else model.enemy
        }

        override fun onStartingSecond() {
            TODO()
        }
    })

    init {
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                val gameSessionDTO = Json.decodeFromString<GameSessionDTO>(string)
                when (gameSessionDTO.type) {
                    GameSessionMessageType.MOVE -> {
                        val move = Move.decodeFromStringToSimplified(
                            gameSessionDTO.message
                        ).restore(model)
                        controller.processEnemyMove(move)
                    }
                    GameSessionMessageType.DISCONNECT -> connection.shutdown()
                    else -> {
                        _systemMessagesLiveData.value = gameSessionDTO.message
                    }
                }


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
            connection.disconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            disconnect()
        }
    }

    private fun Move.Simplified.restore(model: Model): Move {
        val move = when (type) {
            Move.Type.MOTION -> {
                this as MotionMove.Simplified
                val startRow = startCoordinate / 10
                val startColumn = startCoordinate % 10
                val destRow = destinationCoordinate / 10
                val destColumn = destinationCoordinate % 10
                MotionMove(model.board[startRow, startColumn], model.board[destRow, destColumn])
            }
            Move.Type.ADD -> {
                this as AddMove.Simplified
                val destRow = destinationCoordinate / 10
                val destColumn = destinationCoordinate % 10
                AddMove(
                    model.enemy.divisionResources.reserves[divisionType]!!,
                    model.board[destRow, destColumn]
                )
            }
        }
        return move
    }

}

