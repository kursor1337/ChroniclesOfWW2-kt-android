package com.kursor.chroniclesofww2.connection.interfaces

import com.kursor.chroniclesofww2.connection.Host

interface Client {

    val availableHosts: MutableList<Host>

    fun startDiscovery()

    fun connectTo(host: Host, password: String? = null)

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onException(e: Exception)
        fun onHostDiscovered(host: Host)
        fun onHostLost(host: Host)
        fun onFail(errorCode: Int)
    }

}