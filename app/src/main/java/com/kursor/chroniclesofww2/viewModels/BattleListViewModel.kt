package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.data.repositories.BattleRepository
import com.kursor.chroniclesofww2.model.data.Battle

class BattleListViewModel(battleRepository: BattleRepository) : ViewModel() {

    private val battleLiveData = MutableLiveData<List<Battle>>().apply {
        value = battleRepository.battleList
    }

    fun getBattleLiveData(): LiveData<List<Battle>> = battleLiveData

    fun changeDataSource(battleRepository: BattleRepository) {
        battleLiveData.value = battleRepository.battleList
    }

}