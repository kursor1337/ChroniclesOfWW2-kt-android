package com.kursor.chroniclesofww2.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import kotlinx.coroutines.launch

class HostDiscoveryViewModel(val localClient: LocalClient) : ViewModel() {


    private val _hostList = mutableListOf<Host>()
    val hostList: List<Host> get() = _hostList

    var observer: RecyclerViewViewModelObserver? = null

    private val clientDiscoveryListener = object : LocalClient.DiscoveryListener {
        override fun onHostDiscovered(host: Host) {
            Log.d("HostDiscoveryViewModel", "onHostDiscovered: $host")
            _hostList.add(host)
            observer?.itemInserted(_hostList.lastIndex)
        }

        override fun onHostLost(host: Host) {
            Log.d("HostDiscoveryViewModel", "onHostLost: $host")
            val index = _hostList.indexOf(host)
            _hostList.removeAt(index)
            observer?.itemRemoved(index)
        }
    }

    init {
        localClient.discoveryListeners.add(clientDiscoveryListener)
    }


    fun startDiscovery() {
        viewModelScope.launch {
            localClient.startDiscovery()
        }
    }

    fun stopDiscovery() {
        viewModelScope.launch {
            localClient.stopDiscovery()
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            localClient.stopDiscovery()
        }
    }
}