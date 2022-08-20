package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.domain.connection.IHost
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import kotlinx.coroutines.launch

class HostDiscoveryViewModel(val localClient: LocalClient) : ViewModel() {


    private val hostList = mutableListOf<IHost>()

    var observer: RecyclerViewViewModelObserver? = null

    private val clientDiscoveryListener = object : LocalClient.DiscoveryListener {
        override fun onHostDiscovered(host: IHost) {
            hostList.add(host)
            observer?.itemInserted(hostList.lastIndex)
        }

        override fun onHostLost(host: IHost) {
            val index = hostList.indexOf(host)
            hostList.removeAt(index)
            observer?.itemRemoved(index)
        }
    }

    init {
        localClient.discoveryListeners.add(clientDiscoveryListener)
    }

    fun getHostList(): List<IHost> = hostList

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

    fun connectTo(host: IHost) {
        viewModelScope.launch {
            localClient.connectTo(host)
        }
    }
}