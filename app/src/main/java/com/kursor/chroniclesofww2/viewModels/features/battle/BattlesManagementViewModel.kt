package com.kursor.chroniclesofww2.viewModels.features.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.DeleteLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote.PublishBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote.UnpublishBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadMyRemoteBattlesListUseCase
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class BattlesManagementViewModel(
    val loadLocalCustomBattleListUseCase: LoadLocalCustomBattleListUseCase,
    val loadPublishedBattleListUseCase: LoadMyRemoteBattlesListUseCase,
    val deleteLocalCustomBattleUseCase: DeleteLocalCustomBattleUseCase,
    val unpublishBattleUseCase: UnpublishBattleUseCase,
    val publishBattleUseCase: PublishBattleUseCase
) : ViewModel() {

    private val _battleListLiveData = MutableLiveData<List<Battle>>()
    val battleListLiveData: LiveData<List<Battle>> get() = _battleListLiveData

    private val _selectedBattleIndexesLiveData = MutableLiveData<Set<Int>>()
    val selectedBattleIndexesLiveData: LiveData<Set<Int>> get() = _selectedBattleIndexesLiveData

    init {
        loadBattleList(DataSource.LOCAL)
    }

    var currentDataSource = DataSource.LOCAL
        private set

    fun selectOrUnselectBattleIndex(index: Int) {
        if (selectedBattleIndexesLiveData.value?.contains(index) == true) {
            unselectBattleIndex(index)
        } else selectBattleIndex(index)
    }

    private fun selectBattleIndex(index: Int) {
        val selectedBattleList =
            (selectedBattleIndexesLiveData.value ?: emptySet()).toMutableSet().apply {
                add(index)
            }
        _selectedBattleIndexesLiveData.value = selectedBattleList
    }

    private fun unselectBattleIndex(index: Int) {
        val selectedBattleList =
            (selectedBattleIndexesLiveData.value ?: emptySet()).toMutableSet().apply {
                remove(index)
            }
        _selectedBattleIndexesLiveData.value = selectedBattleList
    }

    fun loadBattleList(dataSource: DataSource) {
        currentDataSource = dataSource
        viewModelScope.launch {
            when (dataSource) {
                DataSource.LOCAL -> {
                    loadLocalCustomBattleListUseCase()
                        .onSuccess {
                            _battleListLiveData.postValue(it)
                        }
                }
                DataSource.REMOTE -> {
                    loadPublishedBattleListUseCase()
                        .onSuccess {
                            _battleListLiveData.postValue(it)
                        }
                }
            }
        }
    }

    fun deleteBattleByPosition(position: Int) {
        val battleList = battleListLiveData.value ?: return
        viewModelScope.launch {
            when (currentDataSource) {
                DataSource.LOCAL -> deleteLocalCustomBattleUseCase(battleList[position])
                DataSource.REMOTE -> unpublishBattleUseCase(battleList[position])
            }
            loadBattleList(currentDataSource)
        }
    }

    fun publishSelectedBattles() {
        viewModelScope.launch {
            _selectedBattleIndexesLiveData.value?.forEach {
                val battle = battleListLiveData.value?.get(it) ?: return@launch
                publishBattleUseCase(
                    battleName = battle.name,
                    battleDescription = battle.description,
                    battleData = battle.data
                )
            }
        }
    }

    enum class DataSource {
        LOCAL, REMOTE
    }

}