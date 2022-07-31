package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.data.GameData

class GameDataViewModel : ViewModel() {

    lateinit var myName: String
    lateinit var enemyName: String
    lateinit var battleData: Battle.Data
    var boardHeight: Int = 0
    var boardWidth: Int = 0
    var invertNations: Boolean = false
    var meInitiator: Boolean = true

    fun createGameData(): GameData? {
        return try {
            GameData(
                myName,
                enemyName,
                battleData,
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