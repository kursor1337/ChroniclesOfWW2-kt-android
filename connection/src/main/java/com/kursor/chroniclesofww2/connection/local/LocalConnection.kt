package com.kursor.chroniclesofww2.connection.local

import android.util.Log
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.Connection.Companion.DISCONNECT
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.println
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.*

class LocalConnection(
    val input: BufferedReader,
    val output: BufferedWriter,
    val host: Host,
    val ioDispatcher: CoroutineDispatcher,
    override var sendListener: Connection.SendListener?
) : Connection {

    var receiving = true

    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    init {
        Log.i("Connection", "Init connection")
    }

    override fun shutdown() {
        super.shutdown()
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
            if (string == DISCONNECT) {
                shutdown()
            }
            Log.d("Receiver", "RECEIVED ==> $string")
            emit(string)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun send(string: String) {
        withContext(ioDispatcher) {
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