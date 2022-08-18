package com.kursor.chroniclesofww2.connection.interfaces

import com.kursor.chroniclesofww2.connection.Host

interface LocalClient {

    val availableHosts: MutableList<Host>
    val listener: Listener
    val discoveryListeners: MutableList<DiscoveryListener>

    fun startDiscovery()

    fun stopDiscovery()

    fun connectTo(host: Host, password: String? = null)

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onException(e: Exception)
        fun onFail(errorCode: Int)
    }

    interface DiscoveryListener {
        fun onHostDiscovered(host: Host)
        fun onHostLost(host: Host)
    }

}