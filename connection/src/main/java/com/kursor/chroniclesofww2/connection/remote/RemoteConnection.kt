package com.kursor.chroniclesofww2.connection.remote

import android.util.Log
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.features.Routes
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow


class RemoteConnection(
    val fullUrl: String,
    val httpClient: HttpClient,
    val dispatcher: CoroutineDispatcher,
    val token: String
) : Connection {

    override var sendListener: Connection.SendListener? = null
    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var webSocketSession: WebSocketSession

    suspend fun init() = withContext(Dispatchers.IO) {
        Log.d(TAG, "init: initiating connection to the path $fullUrl")
        webSocketSession = httpClient.webSocketSession {
            url(fullUrl)
            bearerAuth(token = token)
        }

    }

    override suspend fun send(string: String) = withContext(Dispatchers.IO) {
        withContext(dispatcher) {
            webSocketSession.send(string)
        }
    }

    override fun shutdown() {
        coroutineScope.launch {
            webSocketSession.close()
        }
    }

    override fun observeIncoming(): Flow<String> = webSocketSession.incoming
        .receiveAsFlow()
        .filter { it is Frame.Text }
        .map { (it as Frame.Text).readText() }

    companion object {
        const val TAG = "RemoteConnection"
    }

}