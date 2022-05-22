package com.kursor.chroniclesofww2.connection.interfaces

import android.os.Handler
import android.os.Looper
import com.kursor.chroniclesofww2.connection.Host
import java.io.BufferedWriter

interface Connection {

    val sendListener: SendListener?
    val receiveListener: ReceiveListener?
    val host: Host
    val handler: Handler


    fun send(string: String)

    fun dispose()

    /**
     * Listeners for sending and receiving info
     * make sure that almost always these methods are invoked in another thread
     */
    interface SendListener {
        fun onSendSuccess()
        fun onSendFailure(e: Exception)
    }

    fun interface ReceiveListener {
        fun onReceive(string: String)
    }

    companion object {
        val EMPTY_SEND_LISTENER = object : SendListener {
            override fun onSendSuccess() {}

            override fun onSendFailure(e: Exception) {}
        }

        val EMPTY_RECEIVE_LISTENER = ReceiveListener { }

    }
}


const val SERVICE_NAME = "ChroniclesOfWWII"
const val SERVICE_TYPE = "_http._tcp."
const val TAG = "NsdHelper"

fun BufferedWriter.println(string: String) = write(string + "\n")

