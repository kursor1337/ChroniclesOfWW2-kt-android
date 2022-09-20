package com.kursor.chroniclesofww2.viewModels.features.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.user.DeleteAccountUseCase
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _statusLiveData = MutableLiveData<String>()
    val statusLiveData: LiveData<String> get() = _statusLiveData

    private var login = ""
    private var password = ""
    private var repeatPassword = ""

    fun setLogin(login: String) {
        this.login = login
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setRepeatPassword(repeatPassword: String) {
        this.repeatPassword = repeatPassword
    }

    fun ready() {
        if (password != repeatPassword) return
        viewModelScope.launch {
            deleteAccountUseCase(login, password)
                .onSuccess {
                    _statusLiveData.postValue(it.message)
                }.onFailure {
                    _statusLiveData.postValue("Server connection error")
                }
        }
    }

}