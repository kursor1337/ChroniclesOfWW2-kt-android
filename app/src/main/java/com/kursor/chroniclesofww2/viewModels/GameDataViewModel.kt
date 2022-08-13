package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.model.serializable.GameData

class GameDataViewModel : ViewModel() {

    val myNameLiveData = MutableLiveData<String>()
    val enemyNameLiveData = MutableLiveData<String>()
    val battleLiveData = MutableLiveData<Battle>()
    var boardHeight: Int = 0
    var boardWidth: Int = 0
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

}