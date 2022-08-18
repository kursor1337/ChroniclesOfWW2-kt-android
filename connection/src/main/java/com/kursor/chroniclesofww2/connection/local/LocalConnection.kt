package com.kursor.chroniclesofww2.connection.local

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.println
import kotlinx.coroutines.*
import java.io.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class LocalConnection(
    input: BufferedReader,
    output: BufferedWriter,
    val host: Host,
    override var sendListener: Connection.SendListener? = null,
    ioDispatcher: CoroutineDispatcher
) : Connection {

    override var receiveListeners = mutableListOf<Connection.ReceiveListener>()
    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    val coroutineScope = CoroutineScope(ioDispatcher)

    private val sender = Sender(output)
    private val receiver = Receiver(input)

    init {
        Log.i("Connection", "Init connection")
        receiver.startReceiving()
    }

    override fun send(string: String) {
        sender.send(string)
    }

    override fun shutdown() {
        Log.i("Connection", "Disposing")
        receiver.dispose()
        Log.i("Connection", "Disposed")
    }

    inner class Sender(private val output: BufferedWriter) {

        fun send(string: String) {
            coroutineScope.launch {
                try {
                    Log.e("Sender", "Connected, Sending: $string")
                    output.println(string)
                    output.flush()
                    if (sendListener != null) {
                        withContext(Dispatchers.IO) { sendListener?.onSendSuccess() }
                    }
                    Log.e("Sender", "Send Successful: $string")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i("Sender", "_____")
                    withContext(Dispatchers.Main) {
                        sendListener!!.onSendFailure(e)
                    }
                    e.printStackTrace()
                }
            }

        }
    }

    inner class Receiver(private val input: BufferedReader) {

        fun startReceiving() {
            coroutineScope.launch {
                try {
                    while (receiving) {
                        val string = input.readLine()
                        Log.e("Receiver", "RECEIVED ==> $string")
                        withContext(Dispatchers.Main) {
                            receiveListeners.forEach {
                                it.onReceive(string)
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        private var receiving = true

        fun dispose() {
            Log.i("Receiver", "Disposing")
            try {
                receiving = false
                input.close()
                Log.i("Receiver", "Thread stopped")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}