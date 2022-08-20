package com.kursor.chroniclesofww2.domain.connection

import java.lang.Exception

interface LocalServer {


    var listener: Listener?


    suspend fun startListening(name: String)

    suspend fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection) {}
        fun onStartedListening() {}
        fun onListeningStartError(e: Exception) {}
        fun onFail() {}
    }

}