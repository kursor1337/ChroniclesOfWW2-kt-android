package com.kursor.chroniclesofww2.viewModels.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadRemoteCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadStandardBattleListUseCase
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class BattleListViewModel(
    private val loadStandardBattleListUseCase: LoadStandardBattleListUseCase,
    private val loadLocalCustomBattleListUseCase: LoadLocalCustomBattleListUseCase,
    private val loadRemoteCustomBattleListUseCase: LoadRemoteCustomBattleListUseCase
) : ViewModel() {

    private val battleLiveData = MutableLiveData<List<Battle>>()

    fun getBattleLiveData(): LiveData<List<Battle>> = battleLiveData


    fun changeDataSource(index: Int) {
        when (index) {
            STANDARD -> battleLiveData.value = loadStandardBattleListUseCase()
            LOCAL_CUSTOM -> battleLiveData.value = loadLocalCustomBattleListUseCase()
            REMOTE_CUSTOM -> {
                viewModelScope.launch {
                    loadRemoteCustomBattleListUseCase()
                        .onSuccess {
                            battleLiveData.value = it
                        }.onFailure {

                        }
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