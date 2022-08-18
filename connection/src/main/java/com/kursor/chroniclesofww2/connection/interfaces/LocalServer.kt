package com.kursor.chroniclesofww2.connection.interfaces

import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import java.lang.Exception

interface LocalServer {

    val name: String
    val password: String?
    val listener: Listener


    fun startListening()

    fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onStartedListening(host: Host)
        fun onStartedListening()
        fun onListeningStartError(e: Exception)
        fun onFail() {}
    }

}