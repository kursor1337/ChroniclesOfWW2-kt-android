package com.kursor.chroniclesofww2.viewModels.features.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.user.RegisterUseCase
import com.kursor.chroniclesofww2.features.RegisterReceiveDTO
import com.kursor.chroniclesofww2.features.RegisterResponseDTO
import kotlinx.coroutines.launch

class RegisterViewModel(val registerUseCase: RegisterUseCase) : ViewModel() {

    var login = ""

    var username = ""

    var password = ""

    var repeatPassword = ""

    private val _registerResponseLiveData = MutableLiveData<RegisterResponseDTO>()
    val registerResponseLiveData: LiveData<RegisterResponseDTO> get() = _registerResponseLiveData

    fun register(): Boolean {
        if (password != repeatPassword) return false
        viewModelScope.launch {
            registerUseCase(
                RegisterReceiveDTO(
                    login, username, password
                )
            ).onSuccess {
                _registerResponseLiveData.value = it
            }
        }
        return true
    }

}