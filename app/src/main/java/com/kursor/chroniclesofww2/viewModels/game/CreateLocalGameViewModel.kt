package com.kursor.chroniclesofww2.viewModels.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.game.CreateLocalGameUseCase
import kotlinx.coroutines.launch

class CreateLocalGameViewModel(
    val createLocalGameUseCase: CreateLocalGameUseCase
) : ViewModel() {


    fun createGame() {
        viewModelScope.launch {
            createLocalGameUseCase()
        }
    }

}