package com.kursor.chroniclesofww2.domain.connection


interface LocalClient {

    val availableHosts: MutableList<IHost>
    var listener: Listener?
    val discoveryListeners: MutableList<DiscoveryListener>

    suspend fun startDiscovery()

    suspend fun stopDiscovery()

    suspend fun connectTo(host: IHost)

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onException(e: Exception)
        fun onFail(errorCode: Int)
    }

    interface DiscoveryListener {
        fun onHostDiscovered(host: IHost)
        fun onHostLost(host: IHost)
    }

}