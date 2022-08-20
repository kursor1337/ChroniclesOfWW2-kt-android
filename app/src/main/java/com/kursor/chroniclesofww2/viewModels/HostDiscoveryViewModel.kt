package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.domain.connection.IHost
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import kotlinx.coroutines.launch

class HostDiscoveryViewModel(val localClient: LocalClient) : ViewModel() {


    private val _hostList = mutableListOf<IHost>()
    val hostList: List<IHost> get() = _hostList

    var observer: RecyclerViewViewModelObserver? = null

    private val clientDiscoveryListener = object : LocalClient.DiscoveryListener {
        override fun onHostDiscovered(host: IHost) {
            _hostList.add(host)
            observer?.itemInserted(_hostList.lastIndex)
        }

        override fun onHostLost(host: IHost) {
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
}