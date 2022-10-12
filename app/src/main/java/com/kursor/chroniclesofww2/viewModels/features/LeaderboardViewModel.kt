package com.kursor.chroniclesofww2.viewModels.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {

    private val _topPlayersLiveData = MutableLiveData<List<UserInfo>>()
    val topPlayersLiveData: LiveData<List<UserInfo>> get() = _topPlayersLiveData

    fun loadData() {
        viewModelScope.launch {

        }
    }

}