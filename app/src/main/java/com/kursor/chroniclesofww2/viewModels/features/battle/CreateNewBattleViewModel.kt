package com.kursor.chroniclesofww2.viewModels.features.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.SaveLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class CreateNewBattleViewModel(
    val saveLocalCustomBattleUseCase: SaveLocalCustomBattleUseCase
) : ViewModel() {


    private val _statusLiveData = MutableLiveData<Pair<Status, Any?>>()
    val statusLiveData: LiveData<Pair<Status, Any?>> get() = _statusLiveData

    var nations = Nation.values()
    private var nation1: Nation? = null
    private var nation2: Nation? = null

    private var newBattleName = ""
    private var newBattleDescription = ""
    private var player1infantry = 0
    private var player1armored = 0
    private var player1artillery = 0
    private var player2infantry = 0
    private var player2armored = 0
    private var player2artillery = 0

    fun setNation1ByPosition(position: Int) {
        nation1 = nations[position]
    }

    fun setNation2ByPosition(position: Int) {
        nation2 = nations[position]
    }

    fun clearNation1() {
        nation1 = null
    }

    fun clearNation2() {
        nation2 = null
    }

    fun setBattleName(name: String) {
        newBattleName = name
    }

    fun setBattleDescription(description: String) {
        newBattleDescription = description
    }

    fun setPlayer1Infantry(player1InfantryString: String) {
        player1infantry = player1InfantryString.ifBlank { "0" }.toInt()
    }

    fun setPlayer1Armored(player1ArmoredString: String) {
        player1armored = player1ArmoredString.ifBlank { "0" }.toInt()
    }

    fun setPlayer1Artillery(player1ArtilleryString: String) {
        player1artillery = player1ArtilleryString.ifBlank { "0" }.toInt()
    }

    fun setPlayer2Infantry(player2InfantryString: String) {
        player2infantry = player2InfantryString.ifBlank { "0" }.toInt()
    }

    fun setPlayer2Armored(player2ArmoredString: String) {
        player2armored = player2ArmoredString.ifBlank { "0" }.toInt()
    }

    fun setPlayer2Artillery(player2ArtilleryString: String) {
        player2artillery = player2ArtilleryString.ifBlank { "0" }.toInt()
    }

    fun readyAndSave() {
        val battle = createBattle() ?: return
        saveBattle(battle)
        ready(battle)
    }

    private fun ready(battle: Battle) {
        _statusLiveData.postValue(Status.BATTLE_CREATED to battle)
    }

    fun ready() {
        val battle = createBattle() ?: return
        ready(battle)
    }

    private fun saveBattle(battle: Battle) {
        viewModelScope.launch {
            saveLocalCustomBattleUseCase(battle)
        }
    }

    private fun createBattle(): Battle? {
        if (nation1 == null || nation2 == null) return null
        if (nation1 == nation2) {
            _statusLiveData.postValue(Status.SAME_NATIONS_AS_ENEMIES to null)
            return null
        }

        if (player1infantry + player1armored + player1artillery < 1 ||
            player2infantry + player2armored + player2artillery < 1
        ) {
            _statusLiveData.postValue(Status.TOO_LITTLE_DIVISIONS to null)
            return null
        }
        return Battle(
            id = 0,
            name = newBattleName,
            description = newBattleDescription,
            nation1 = nation1!!,
            nation1divisions = mapOf(
                Division.Type.INFANTRY to player1infantry,
                Division.Type.ARMORED to player1armored,
                Division.Type.ARTILLERY to player1artillery
            ),
            nation2 = nation2!!,
            nation2divisions = mapOf(
                Division.Type.INFANTRY to player2infantry,
                Division.Type.ARMORED to player2armored,
                Division.Type.ARTILLERY to player2artillery
            )
        )
    }

    enum class Status {
        SAME_NATIONS_AS_ENEMIES, TOO_LITTLE_DIVISIONS, BATTLE_CREATED
    }

}