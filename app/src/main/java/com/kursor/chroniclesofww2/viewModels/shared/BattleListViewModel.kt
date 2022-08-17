package com.kursor.chroniclesofww2.viewModels.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.data.repositories.battle.BattleRepository
import com.kursor.chroniclesofww2.model.serializable.Battle

class BattleListViewModel : ViewModel() {

    private val battleLiveData = MutableLiveData<List<Battle>>()

    fun getBattleLiveData(): LiveData<List<Battle>> = battleLiveData

    fun changeDataSource(battleRepository: BattleRepository) {
        battleLiveData.value = battleRepository.battleList
    }

}