package com.kursor.chroniclesofww2.connection

import java.io.BufferedWriter

interface Connection {

    val sendListener: SendListener
    val receiveListener: ReceiveListener


    fun send(string: String)

    fun dispose()

    interface SendListener {
        fun onSendSuccess()
        fun onSendFailure(e: Exception)
    }

    interface ReceiveListener {
        fun onReceive(string: String)
    }
}


fun BufferedWriter.println(string: String) = write(string + "\n")

