package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.model.serializable.Battle

class BattleViewModel : ViewModel() {

    val battleLiveData = MutableLiveData<Battle>()

}