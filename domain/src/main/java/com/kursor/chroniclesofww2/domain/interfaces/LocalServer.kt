package com.kursor.chroniclesofww2.domain.interfaces

import java.lang.Exception

interface LocalServer {

    val name: String
    val password: String?
    val listener: Listener


    fun startListening()

    fun stopListening()

    interface Listener {
        fun onConnectionEstablished(connection: Connection) {}
        fun onStartedListening() {}
        fun onListeningStartError(e: Exception) {}
        fun onFail() {}
    }

}