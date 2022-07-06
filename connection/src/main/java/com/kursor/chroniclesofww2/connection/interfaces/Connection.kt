package com.kursor.chroniclesofww2.connection.interfaces

import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import java.io.BufferedWriter

interface Connection {

    var sendListener: SendListener?
    var receiveListener: ReceiveListener?
    val shutdownListeners: MutableList<ShutdownListener>
    val host: Host
    val handler: Handler

    fun send(string: String)

    fun shutdown()

    /**
     * Listeners for sending and receiving info
     * make sure that almost always these methods are invoked in another thread
     */
    interface SendListener {
        fun onSendSuccess()
        fun onSendFailure(e: Exception)
    }

    interface ReceiveListener {
        fun onReceive(string: String)
        fun onDisconnected()
    }

    fun interface ShutdownListener {
        fun onConnectionDisposed()
    }

    companion object {
        val EMPTY_SEND_LISTENER = object : SendListener {
            override fun onSendSuccess() {}

            override fun onSendFailure(e: Exception) {}
        }

        val EMPTY_RECEIVE_LISTENER = object : ReceiveListener {
            override fun onReceive(string: String) {}
            override fun onDisconnected() {}
        }

        var currentConnection: Connection? = null
            set(value) {
                if (value == null) field?.shutdown()
                field = value
            }

    }
}


const val SERVICE_NAME = "ChroniclesOfWWII"
const val SERVICE_TYPE = "_http._tcp."
const val TAG = "NsdHelper"

fun BufferedWriter.println(string: String) = write(string + "\n")

