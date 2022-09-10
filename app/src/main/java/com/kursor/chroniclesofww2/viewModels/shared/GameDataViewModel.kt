package com.kursor.chroniclesofww2.viewModels.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.model.serializable.GameData

class GameDataViewModel : ViewModel() {

    val myNameLiveData = MutableLiveData<String>()
    val enemyNameLiveData = MutableLiveData<String>()
    val battleLiveData = MutableLiveData<Battle>()
    var boardHeight: Int = Board.DEFAULT_SIZE
    var boardWidth: Int = Board.DEFAULT_SIZE
    var invertNations: Boolean = false
    var meInitiator: Boolean = true

    fun createGameData(): GameData? {
        return try {
            GameData(
                myNameLiveData.value!!,
                enemyNameLiveData.value!!,
                battleLiveData.value!!,
                boardHeight,
                boardWidth,
                invertNations,
                meInitiator
            )
        } catch (e: Exception) {
            return null
        }
    }

    fun dataContainer(): DataContainer = DataContainer(
        myName = myNameLiveData.value,
        enemyName = enemyNameLiveData.value,
        battle = battleLiveData.value,
        boardHeight = boardHeight,
        boardWidth = boardWidth,
        invertNations = invertNations,
        meInitiator = meInitiator
    )

    data class DataContainer(
        var myName: String?,
        var enemyName: String?,
        var battle: Battle?,
        var boardHeight: Int,
        var boardWidth: Int,
        var invertNations: Boolean,
        var meInitiator: Boolean
    ) {
        fun createGameData(): GameData? {
            return try {
                GameData(
                    myName!!,
                    enemyName!!,
                    battle!!,
                    boardHeight,
                    boardWidth,
                    invertNations,
                    meInitiator
                )
            } catch (e: Exception) {
                return null
            }
        }
    }
}