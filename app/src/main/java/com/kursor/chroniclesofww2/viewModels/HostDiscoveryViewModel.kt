package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.domain.interfaces.LocalClient

class HostDiscoveryViewModel(val localClient: LocalClient) : ViewModel() {


    private val hostList = mutableListOf<Host>()

    var observer: RecyclerViewViewModelObserver? = null

    private val clientDiscoveryListener = object : LocalClient.DiscoveryListener {
        override fun onHostDiscovered(host: Host) {
            hostList.add(host)
            observer?.itemInserted(hostList.lastIndex)
        }

        override fun onHostLost(host: Host) {
            val index = hostList.indexOf(host)
            hostList.removeAt(index)
            observer?.itemRemoved(index)
        }
    }

    init {
        localClient.discoveryListeners.add(clientDiscoveryListener)
    }

    fun getHostList(): List<Host> = hostList
}