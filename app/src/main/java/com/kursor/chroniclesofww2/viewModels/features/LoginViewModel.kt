package com.kursor.chroniclesofww2.viewModels.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.user.LoginUseCase
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import kotlinx.coroutines.launch

class LoginViewModel(val loginUseCase: LoginUseCase) : ViewModel() {

    var login = ""

    var password = ""

    private val _loginResponseDTOLiveData = MutableLiveData<LoginResponseDTO>()
    val loginResponseLiveData: LiveData<LoginResponseDTO> get() = _loginResponseDTOLiveData

    fun login() {
        viewModelScope.launch {
            val loginResponseDTO = loginUseCase(
                LoginReceiveDTO(
                    login = login,
                    password = password
                )
            )
            _loginResponseDTOLiveData.value = loginResponseDTO
        }
    }

}