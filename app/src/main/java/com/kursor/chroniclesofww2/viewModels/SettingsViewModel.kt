package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.auth.CheckIsSignedInUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.ChangeUsernameUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.LoginUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.LogoutUseCase
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SettingsViewModel(
    private val accountRepository: AccountRepository,
    private val checkIsSignedInUseCase: CheckIsSignedInUseCase,
    private val changeUsernameUseCase: ChangeUsernameUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _usernameLiveData = MutableLiveData(accountRepository.username)
    val usernameLiveData: LiveData<String> get() = _usernameLiveData

    //Boolean - isSignedIn, List<String> - args
    private val _isSignedInLiveData = MutableLiveData<Pair<Boolean, List<String>>>()
    val isSignedInLiveData: LiveData<Pair<Boolean, List<String>>> get() = _isSignedInLiveData

    init {
        viewModelScope.launch {
            checkIsSignedInUseCase()
                .onSuccess {
                    _isSignedInLiveData.value = it to mutableListOf(accountRepository.login!!)
                }
                .onFailure {
                    _isSignedInLiveData.value = false to emptyList()
                }
        }
    }

    fun changeUserName(username: String) {
        _usernameLiveData.value = username
    }

    fun logout() {
        logoutUseCase()
    }

    fun save() {
        val username = usernameLiveData.value
        if (username != null) changeUsernameUseCase(username)

    }

    companion object {
        const val LOGIN = 0
    }
}