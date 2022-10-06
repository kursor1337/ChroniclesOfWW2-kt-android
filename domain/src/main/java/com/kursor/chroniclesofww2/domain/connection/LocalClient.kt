package com.kursor.chroniclesofww2.domain.connection


interface LocalClient {

    val availableHosts: List<Host>
    var listener: Listener?
    val discoveryListeners: MutableList<DiscoveryListener>

    suspend fun startDiscovery()

    suspend fun stopDiscovery()

    suspend fun connectTo(host: Host)

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