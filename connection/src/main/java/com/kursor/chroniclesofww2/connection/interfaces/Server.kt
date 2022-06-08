package com.kursor.chroniclesofww2.connection.interfaces

import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import java.lang.Exception

interface Server {

    val name: String
    val password: String?
    val sendListener: Connection.SendListener?
    val receiveListener: Connection.ReceiveListener?
    val listener: Listener
    val handler: Handler


    fun startListening()

    fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onRegistered(host: Host)
        fun onListeningStartError(e: Exception)
        fun onFail() {}
    }

}