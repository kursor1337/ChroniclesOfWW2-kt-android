package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.model.data.Battle

class BattleViewModel : ViewModel() {

    val battleLiveData = MutableLiveData<Battle>()

}