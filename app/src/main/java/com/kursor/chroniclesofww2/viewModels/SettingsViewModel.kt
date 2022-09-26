package com.kursor.chroniclesofww2.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.auth.CheckIsSignedInUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.ChangeUsernameUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.GetUserInfoListUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.LoginUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.LogoutUseCase
import io.ktor.client.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SettingsViewModel(
    private val accountRepository: AccountRepository,
    private val checkIsSignedInUseCase: CheckIsSignedInUseCase,
    private val changeUsernameUseCase: ChangeUsernameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserInfoListUseCase: GetUserInfoListUseCase
) : ViewModel() {

    private val _usernameLiveData = MutableLiveData(accountRepository.username)
    val usernameLiveData: LiveData<String> get() = _usernameLiveData

    //Boolean - isSignedIn, List<String> - args
    private val _isSignedInLiveData = MutableLiveData<Pair<Boolean, List<String>>>()
    val isSignedInLiveData: LiveData<Pair<Boolean, List<String>>> get() = _isSignedInLiveData

    fun checkIsSignedIn() {
        viewModelScope.launch {
            checkIsSignedInUseCase()
                .onSuccess {
                    Log.d("SettingsViewModel", "onSuccess")
                    _isSignedInLiveData.postValue(
                        it to mutableListOf(
                            accountRepository.login ?: ""
                        )
                    )
                }.onConnectionFailure {
                    Log.d("SettingsViewModel", "onFailure")
                    _isSignedInLiveData.postValue(false to emptyList())
                }.onUnauthorized {
                    _isSignedInLiveData.postValue(false to emptyList())
                }
        }
    }

    fun loadAccountData() {
        viewModelScope.launch {

        }
    }

    fun changeUserName(username: String) {
        _usernameLiveData.value = username
    }

    fun logout() {
        logoutUseCase()
        _isSignedInLiveData.value = false to emptyList()
    }

    fun save() {
        val username = usernameLiveData.value
        if (username != null) changeUsernameUseCase(username)

    }

    companion object {
        const val LOGIN = 0
    }
}