package com.kursor.chroniclesofww2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Client

class HostViewModel(val client: Client) : ViewModel() {


    private val hostList = mutableListOf<Host>()

    var observer: RecyclerViewViewModelObserver? = null

    private val clientDiscoveryListener = object : Client.DiscoveryListener {
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
        client.discoveryListeners.add(clientDiscoveryListener)
    }

    fun getHostList(): List<Host> = hostList
}