package com.kursor.chroniclesofww2.connection.interfaces

import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import java.lang.Exception

interface Server {

    val name: String
    val password: String?
    val listener: Listener
    val handler: Handler


    fun startListening()

    fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection)
        fun onStartedListening(host: Host)
        fun onListeningStartError(e: Exception)
        fun onFail() {}
    }

}