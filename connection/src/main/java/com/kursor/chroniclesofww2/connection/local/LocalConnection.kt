package com.kursor.chroniclesofww2.connection.local

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.println
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class LocalConnection(
    val input: BufferedReader,
    val output: BufferedWriter,
    val host: Host,
    ioDispatcher: CoroutineDispatcher,
    override var sendListener: Connection.SendListener?
) : Connection {

    val coroutineScope = CoroutineScope(ioDispatcher)
    var receiving = true

    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    init {
        Log.i("Connection", "Init connection")
    }

    override fun shutdown() {
        receiving = false
        try {
            input.close()
            output.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun observeIncoming(): Flow<String> = flow {
        while (receiving) {
            val string = input.readLine()
            Log.d("Receiver", "RECEIVED ==> $string")
            emit(string)
        }
    }

    override fun send(string: String) {
        coroutineScope.launch {
            try {
                Log.d("Sender", "Connected, Sending: $string")
                output.println(string)
                output.flush()
                if (sendListener != null) {
                    withContext(Dispatchers.IO) { sendListener?.onSendSuccess() }
                }
                Log.d("Sender", "Send Successful: $string")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Sender", "_____")
                withContext(Dispatchers.Main) {
                    sendListener?.onSendFailure(e)
                }
                e.printStackTrace()
            }
        }

    }
}