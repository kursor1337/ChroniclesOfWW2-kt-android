package com.kursor.chroniclesofww2.domain.interfaces


interface LocalClient {

    val availableHosts: MutableList<IHost>
    val listener: Listener
    val discoveryListeners: MutableList<DiscoveryListener>

    fun startDiscovery()

    fun stopDiscovery()

    fun connectTo(host: IHost, password: String? = null)

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