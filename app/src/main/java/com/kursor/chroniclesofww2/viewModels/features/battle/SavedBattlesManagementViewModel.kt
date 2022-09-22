package com.kursor.chroniclesofww2.viewModels.features.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.DeleteLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.viewModels.RecyclerViewViewModelObserver
import kotlinx.coroutines.launch

class SavedBattlesManagementViewModel(
    val loadLocalCustomBattleListUseCase: LoadLocalCustomBattleListUseCase,
    val deleteLocalCustomBattleUseCase: DeleteLocalCustomBattleUseCase
) : ViewModel() {

    private var _battleListLiveData = MutableLiveData<List<Battle>>()
    val battleListLiveData: LiveData<List<Battle>> get() = _battleListLiveData

    init {
        loadBattleList()
    }

    fun loadBattleList() {
        viewModelScope.launch {
            loadLocalCustomBattleListUseCase()
                .onSuccess {
                    _battleListLiveData.postValue(it)
                }
        }
    }

    fun deleteBattleByPosition(position: Int) {
        val battleList = battleListLiveData.value ?: return
        viewModelScope.launch {
            deleteLocalCustomBattleUseCase(battleList[position])
            loadBattleList()
        }
    }

}