package com.kursor.chroniclesofww2.viewModels.game.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.game.CreateGameStatus

class RequestForAcceptViewModel : ViewModel() {

    lateinit var statusLiveData: LiveData<Pair<CreateGameStatus, Any?>>

}