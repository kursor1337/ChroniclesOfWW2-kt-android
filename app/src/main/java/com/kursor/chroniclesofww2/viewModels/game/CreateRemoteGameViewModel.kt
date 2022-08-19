package com.kursor.chroniclesofww2.viewModels.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.game.CreateRemoteGameUseCase
import com.kursor.chroniclesofww2.features.CreateGameReceiveDTO
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.launch

class CreateRemoteGameViewModel(
    val createRemoteGameUseCase: CreateRemoteGameUseCase,
    val accountRepository: AccountRepository
) : ViewModel() {


    private val _createdGameIdLiveData = MutableLiveData<Int>()
    val createdGameIdLiveData: LiveData<Int> get() = _createdGameIdLiveData

    val passwordLiveData = MutableLiveData<String>()
    var battle: Battle? = null

    fun createGame(battle: Battle, boardWidth: Int, boardHeight: Int) {
        viewModelScope.launch {
            val createGameResponseDTO = createRemoteGameUseCase(
                CreateGameReceiveDTO(
                    initiatorLogin = accountRepository.login ?: return@launch,
                    password = passwordLiveData.value ?: "",
                    battle = battle,
                    boardWidth = boardWidth,
                    boardHeight = boardHeight
                )
            )
            _createdGameIdLiveData.value = createGameResponseDTO.gameId
        }
    }

}