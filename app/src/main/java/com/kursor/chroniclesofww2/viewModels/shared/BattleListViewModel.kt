package com.kursor.chroniclesofww2.viewModels.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.interfaces.BattleRepository
import com.kursor.chroniclesofww2.data.repositories.battle.RemoteCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.domain.useCases.battle.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.LoadRemoteCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.LoadStandardBattleListUseCase
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class BattleListViewModel(
    val loadStandardBattleListUseCase: LoadStandardBattleListUseCase,
    val loadLocalCustomBattleListUseCase: LoadLocalCustomBattleListUseCase,
    val loadRemoteCustomBattleListUseCase: LoadRemoteCustomBattleListUseCase
) : ViewModel() {

    private val battleLiveData = MutableLiveData<List<Battle>>()

    fun getBattleLiveData(): LiveData<List<Battle>> = battleLiveData


    fun changeDataSource(index: Int) {
        when (index) {
            STANDARD -> battleLiveData.value = loadStandardBattleListUseCase()
            LOCAL_CUSTOM -> battleLiveData.value = loadLocalCustomBattleListUseCase()
            REMOTE_CUSTOM -> {
                viewModelScope.launch {
                    battleLiveData.value = loadRemoteCustomBattleListUseCase()!!
                }
            }
        }
    }

    companion object {
        const val STANDARD = 0
        const val LOCAL_CUSTOM = 1
        const val REMOTE_CUSTOM = 2
    }

}