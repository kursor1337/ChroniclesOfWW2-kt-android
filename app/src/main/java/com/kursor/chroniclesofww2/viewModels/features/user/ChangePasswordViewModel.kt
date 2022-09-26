package com.kursor.chroniclesofww2.viewModels.features.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.user.ChangePasswordUseCase
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.presentation.ui.fragments.features.user.ChangePasswordFragment
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private var currentPassword = ""
    private var newPassword = ""
    private var repeatPassword = ""

    private val _statusLiveData = MutableLiveData<String>()
    val statusLiveData: LiveData<String> get() = _statusLiveData

    fun setCurrentPassword(password: String) {
        currentPassword = password
    }

    fun setNewPassword(password: String) {
        newPassword = password
    }

    fun setRepeatPassword(password: String) {
        repeatPassword = password
    }

    fun ready() {
        if (newPassword != repeatPassword) return
        viewModelScope.launch {
            changePasswordUseCase(
                ChangePasswordReceiveDTO(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
            ).onSuccess {
                _statusLiveData.postValue(it.message)
            }.onConnectionFailure {
                _statusLiveData.postValue("Couldn't connect to the network")
            }.onUnauthorized {
                _statusLiveData.postValue("Unauthorized")
            }
        }
    }

}