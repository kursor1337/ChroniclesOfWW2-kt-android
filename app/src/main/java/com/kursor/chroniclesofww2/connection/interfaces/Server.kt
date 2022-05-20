package com.kursor.chroniclesofww2.connection.interfaces

import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.local.LocalServer
import java.lang.Exception

interface Server {

    val name: String
    val password: String?
    val sendListener: Connection.SendListener?
    val receiveListener: Connection.ReceiveListener?
    val listener: Listener


    fun startListening()

    fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onRegistered(host: Host)
        fun onListeningStartError(e: Exception)
        fun onFail() {}
    }

}