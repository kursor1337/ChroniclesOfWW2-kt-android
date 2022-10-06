package com.kursor.chroniclesofww2.connection.local

import android.util.Log
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.Connection.Companion.DISCONNECT
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.println
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val receiveSharedFlow: Flow<String>

    init {
        Log.d("Connection", "Init connection")
        receiveSharedFlow = startReceive()
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

    private var flowCounter = 0

    override fun observeIncoming(): Flow<String> = flow {
        receiveSharedFlow.collect {
            this.emit(it)
        }
    }.flowOn(Dispatchers.IO)


    private fun startReceive() = flow {
        while (receiving) {
            val string = input.readLine()
            if (string == DISCONNECT) {
                shutdown()
            }
            Log.d("Receiver", "RECEIVED ==> $string")
            emit(string)
        }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        replay = 0
    )

    override suspend fun send(string: String) {
        withContext(ioDispatcher) {
            try {
                Log.d("LocalConnection", "sent $string")
                output.println(string)
                output.flush()
                if (sendListener != null) {
                    withContext(Dispatchers.IO) { sendListener?.onSendSuccess() }
                }
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