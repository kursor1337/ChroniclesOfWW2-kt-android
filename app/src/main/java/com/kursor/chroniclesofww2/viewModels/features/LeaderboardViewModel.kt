package com.kursor.chroniclesofww2.viewModels.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.useCases.user.GetAccountInfoUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.ObtainLeaderBoardUseCase
import com.kursor.chroniclesofww2.features.AccountInfo
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val obtainLeaderBoardUseCase: ObtainLeaderBoardUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {

    private val _topPlayersLiveData = MutableLiveData<List<UserInfo>>()
    val topPlayersLiveData: LiveData<List<UserInfo>> get() = _topPlayersLiveData

    private val _yourScoreLiveData = MutableLiveData<AccountInfo>()
    val yourScoreLiveData: LiveData<AccountInfo> get() = _yourScoreLiveData

    fun loadData() {
        viewModelScope.launch {
            obtainLeaderBoardUseCase()
                .onSuccess {
                    _topPlayersLiveData.postValue(
                        it.top.take(100)
                    )
                }
            getAccountInfoUseCase()
                .onSuccess {
                    _yourScoreLiveData.postValue(it)
                }
        }
    }

}