package com.kursor.chroniclesofww2.domain.interfaces

import kotlinx.coroutines.flow.Flow
import java.io.BufferedWriter

interface Connection {

    var sendListener: SendListener?
    val shutdownListeners: MutableList<ShutdownListener>

    suspend fun send(string: String)

    fun shutdown() {
        shutdownListeners.forEach {
            it.onConnectionDisposed()
        }
    }

    suspend fun disconnect() {
        send(DISCONNECT)
        shutdown()
    }

    fun observeIncoming(): Flow<String>

    /**
     * Listeners for sending and receiving info
     * make sure that almost always these methods are invoked in another thread
     */


    interface SendListener {
        fun onSendSuccess() {}
        fun onSendFailure(e: Exception) {}
    }

    fun interface ReceiveListener {
        fun onReceive(string: String)
    }

    fun interface ShutdownListener {
        fun onConnectionDisposed()
    }

    companion object {

        var CURRENT: Connection? = null
            set(value) {
                if (value == null) field?.shutdown()
                field = value
            }


        const val DISCONNECT = "Disconnect"

    }
}


const val SERVICE_NAME = "ChroniclesOfWWII"
const val SERVICE_TYPE = "_http._tcp."
const val TAG = "NsdHelper"

fun BufferedWriter.println(string: String) = write(string + "\n")

