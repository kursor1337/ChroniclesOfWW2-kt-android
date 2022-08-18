package com.kursor.chroniclesofww2.viewModels.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.interfaces.BattleRepository
import com.kursor.chroniclesofww2.data.repositories.battle.RemoteCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class BattleListViewModel : ViewModel() {

    private val battleLiveData = MutableLiveData<List<Battle>>()

    fun getBattleLiveData(): LiveData<List<Battle>> = battleLiveData

    fun changeDataSource(battleRepository: BattleRepository) {
        if (battleRepository is RemoteCustomBattleRepositoryImpl) {
            viewModelScope.launch {
                battleRepository.fetch()
                battleLiveData.value = battleRepository.battleList
            }
        }

    }

}